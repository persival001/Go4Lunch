package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.location.GetLocationUseCase;
import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.permissions.IsGpsActivatedUseCase;
import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetParticipantsUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {
    @NonNull
    private final IsGpsActivatedUseCase isGpsActivatedUseCase;
    @NonNull
    private final LiveData<List<RestaurantsViewState>> restaurantsLiveData;

    @Inject
    public RestaurantsViewModel(
        @NonNull GetLocationUseCase getLocationUseCase,
        @NonNull IsGpsActivatedUseCase isGpsActivatedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase,
        @NonNull GetParticipantsUseCase getParticipantsUseCase) {
        this.isGpsActivatedUseCase = isGpsActivatedUseCase;

        LiveData<LocationEntity> locationLiveData = getLocationUseCase.invoke();
        LiveData<HashMap<String, Integer>> participantsLiveData = getParticipantsUseCase.invoke();

        LiveData<List<RestaurantsViewState>> unsortedRestaurantsLiveData = Transformations.switchMap(
            locationLiveData,
            location -> Transformations.switchMap(
                getNearbyRestaurantsUseCase.invoke(),
                places -> Transformations.map(
                    participantsLiveData,
                    participants -> mapPlacesToRestaurantViewStates(location.getLatitude() + "," + location.getLongitude(), places, participants)
                )
            )
        );

        restaurantsLiveData = Transformations.map(
            unsortedRestaurantsLiveData,
            restaurantList -> sortRestaurantViewStates(restaurantList)
        );
    }


    private List<RestaurantsViewState> mapPlacesToRestaurantViewStates(
        String location,
        List<NearbyRestaurantsEntity> places,
        Map<String, Integer> placeIdsCount
    ) {
        List<RestaurantsViewState> restaurantsList = new ArrayList<>();
        for (NearbyRestaurantsEntity restaurant : places) {
            Integer participantCount = 0;
            if (placeIdsCount.containsKey(restaurant.getId())) {
                participantCount = placeIdsCount.get(restaurant.getId());
            }

            restaurantsList.add(
                new RestaurantsViewState(
                    restaurant.getId(),
                    mapFormattedName(restaurant.getName()),
                    restaurant.getAddress(),
                    restaurant.isOpeningHours(),
                    mapHaversineDistance(
                        restaurant.getLatitude(),
                        restaurant.getLongitude(),
                        location
                    ),
                    participantCount.toString(),
                    mapRating(restaurant.getRating()),
                    mapPictureUrl(restaurant.getPhotos())
                )
            );
        }
        return restaurantsList;
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
    private float mapRating(Float rating) {
        if (rating != null) {
            return rating * 3F / 5F;
        } else {
            return 0F;
        }
    }

    // Convert a string to title case like "Restaurant Name"
    private String mapFormattedName(String name) {
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
    public String mapPictureUrl(List<String> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0);
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "";
        }
    }

    // Get the distance between two points
    public String mapHaversineDistance(double restaurantLatitude, double restaurantLongitude, String gpsLocation) {
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

    public LiveData<Boolean> isGpsActivatedLiveData() {
        return isGpsActivatedUseCase.invoke();
    }

}


