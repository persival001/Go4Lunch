package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.location.LocationEntity;
import com.persival.go4lunch.data.location.LocationRepository;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.places.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {

    @NonNull
    private final LocationRepository locationRepository;

    private final LiveData<List<RestaurantsViewState>> restaurantsLiveData;

    @Inject
    public RestaurantsViewModel(
        @NonNull GooglePlacesRepository googlePlacesRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.locationRepository = locationRepository;

        LiveData<LocationEntity> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<String> locationAsString = Transformations.map(
            locationLiveData,
            location -> location.getLatitude() + "," + location.getLongitude()
        );


        LiveData<List<RestaurantsViewState>> unsortedRestaurantsLiveData = Transformations.switchMap(
            locationAsString,
            location -> Transformations.map(
                googlePlacesRepository.getNearbyRestaurants(
                    location,
                    5000,
                    "restaurant",
                    MAPS_API_KEY
                ),
                places -> {
                    List<RestaurantsViewState> restaurantsList = new ArrayList<>();
                    for (NearbyRestaurantsResponse.Place restaurant : places) {
                        if (isValidRestaurant(restaurant)) {
                            restaurantsList.add(
                                new RestaurantsViewState(
                                    restaurant.getId() != null ? restaurant.getId() : "",
                                    restaurant.getName() != null ? restaurant.getName() : "",
                                    restaurant.getAddress() != null ? restaurant.getAddress() : "",
                                    getOpeningTime(restaurant.getOpeningHours()),
                                    getHaversineDistance(
                                        restaurant.getLatitude(),
                                        restaurant.getLongitude(),
                                        location
                                    ),
                                    "(2)",
                                    getRating(restaurant.getRating()),
                                    getPictureUrl(restaurant.getPhotos()) != null ? getPictureUrl(restaurant.getPhotos()) : ""
                                )
                            );
                        }
                    }

                    return restaurantsList;
                }
            )
        );

        restaurantsLiveData = Transformations.map(unsortedRestaurantsLiveData, restaurantsList -> {
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
        });
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

    // Verify that the restaurant has all the necessary information
    private boolean isValidRestaurant(NearbyRestaurantsResponse.Place restaurant) {
        return restaurant.getId() != null &&
            restaurant.getName() != null &&
            restaurant.getAddress() != null &&
            restaurant.getOpeningHours() != null &&
            restaurant.getLatitude() != 0 &&
            restaurant.getLongitude() != 0 &&
            restaurant.getRating() != null &&
            restaurant.getPhotos() != null &&
            !restaurant.getPhotos().isEmpty();
    }

    private boolean getOpeningTime(NearbyRestaurantsResponse.OpeningHours openingHours) {
        return openingHours != null && openingHours.isOpenNow();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }

}


