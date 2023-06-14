package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetParticipantsUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {
    @NonNull
    private final GetLocationUseCase getLocationUseCase;
    @NonNull
    private final GetParticipantsUseCase getParticipantsUseCase;
    @NonNull
    private final GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;
    @NonNull
    private final IsGpsActivatedUseCase isGpsActivatedUseCase;
    private final LiveData<List<RestaurantsViewState>> restaurantsLiveData;
    public LiveData<String> participantsLiveData;


    @Inject
    public RestaurantsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull GetParticipantsUseCase getParticipantsUseCase,
        @NonNull IsGpsActivatedUseCase isGpsActivatedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase
    ) {
        this.getLocationUseCase = getLocationUseCase;
        this.getParticipantsUseCase = getParticipantsUseCase;
        this.isGpsActivatedUseCase = isGpsActivatedUseCase;
        this.getNearbyRestaurantsUseCase = getNearbyRestaurantsUseCase;
        this.restaurantsLiveData = setupRestaurantsLiveData();
    }

    private LiveData<List<RestaurantsViewState>> setupRestaurantsLiveData() {
        LiveData<LocationEntity> locationLiveData = getLocationUseCase.invoke();
        LiveData<String> locationAsString = transformLocationToString(locationLiveData);
        LiveData<List<RestaurantsViewState>> unsortedRestaurantsLiveData = getRestaurantViewStateLiveData(locationAsString);
        return sortRestaurantsLiveData(unsortedRestaurantsLiveData);
    }

    private LiveData<String> transformLocationToString(
        LiveData<LocationEntity> locationLiveData
    ) {
        return Transformations.map(
            locationLiveData,
            location -> location.getLatitude() + "," + location.getLongitude()
        );
    }

    private LiveData<List<RestaurantsViewState>> getRestaurantViewStateLiveData(
        LiveData<String> locationAsString
    ) {
        return Transformations.switchMap(
            locationAsString,
            locationStr -> Transformations.map(
                getNearbyRestaurantsUseCase.invoke(),
                places -> {
                    List<RestaurantsViewState> restaurantsList = new ArrayList<>();
                    for (NearbyRestaurantsResponse.Place restaurant : places) {
                        if (restaurant.getId() != null &&
                            restaurant.getName() != null &&
                            restaurant.getAddress() != null
                        ) {
                            restaurantsList.add(
                                new RestaurantsViewState(
                                    restaurant.getId(),
                                    getFormattedName(restaurant.getName()),
                                    restaurant.getAddress(),
                                    getOpeningTime(restaurant.getOpeningHours()),
                                    getHaversineDistance(
                                        restaurant.getLatitude(),
                                        restaurant.getLongitude(),
                                        locationStr
                                    ),
                                    "0",
                                    getRating(restaurant.getRating()),
                                    getPictureUrl(restaurant.getPhotos())
                                )
                            );
                        }
                    }
                    return restaurantsList;
                }
            )
        );
    }
    
    private LiveData<List<RestaurantsViewState>> sortRestaurantsLiveData(
        LiveData<List<RestaurantsViewState>> unsortedRestaurantsLiveData
    ) {
        return Transformations.map(unsortedRestaurantsLiveData, this::sortRestaurantViewStates);
    }

    private List<RestaurantsViewState> sortRestaurantViewStates(List<RestaurantsViewState> restaurantsList) {
        if (restaurantsList == null) {
            return null;
        }
        List<RestaurantsViewState> sortedRestaurantsList = new ArrayList<>(restaurantsList);
        Collections.sort(sortedRestaurantsList, (r1, r2) -> {
            String distance1Str = r1.getDistance().replaceAll("\\s+m", "");
            String distance2Str = r2.getDistance().replaceAll("\\s+m", "");

            double distance1 = Double.parseDouble(distance1Str);
            double distance2 = Double.parseDouble(distance2Str);

            return Double.compare(distance1, distance2);
        });
        return sortedRestaurantsList;
    }

    public LiveData<List<RestaurantsViewState>> getSortedRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    // Convert rating from 5 to 3 stars
    private float getRating(Float rating) {
        if (rating != null) {
            return rating * 3F / 5F;
        } else {
            return 0F;
        }
    }

    // Convert a string to title case like "Restaurant Name"
    private String getFormattedName(String name) {
        if (name != null) {
            StringBuilder titleCase = new StringBuilder();
            boolean nextTitleCase = true;

            for (char c : name.toLowerCase().toCharArray()) {
                if (Character.isSpaceChar(c)) {
                    nextTitleCase = true;
                } else if (nextTitleCase) {
                    c = Character.toTitleCase(c);
                    nextTitleCase = false;
                }

                titleCase.append(c);
            }

            return titleCase.toString();
        } else {
            return "";
        }
    }

    // Get a photo reference if it exists and convert it to a picture url
    public String getPictureUrl(List<NearbyRestaurantsResponse.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "";
        }
    }

    // Get the distance between two points
    public String getHaversineDistance(double restaurantLatitude, double restaurantLongitude, String gpsLocation) {
        String[] locationSplit = gpsLocation.split(",");
        double gpsLatitude = Double.parseDouble(locationSplit[0]);
        double gpsLongitude = Double.parseDouble(locationSplit[1]);
        final int R = 6371_000; // Radius of the earth in meters
        double latDistance = Math.toRadians(gpsLatitude - restaurantLatitude);
        double lonDistance = Math.toRadians(gpsLongitude - restaurantLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(restaurantLatitude)) * Math.cos(Math.toRadians(gpsLatitude))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return String.format(Locale.getDefault(), "%.0f", distance) + " m";
    }

    private boolean getOpeningTime(NearbyRestaurantsResponse.OpeningHours openingHours) {
        return openingHours != null && openingHours.isOpenNow();
    }

    public LiveData<Boolean> isGpsActivatedLiveData() {
        return isGpsActivatedUseCase.invoke();
    }

}


