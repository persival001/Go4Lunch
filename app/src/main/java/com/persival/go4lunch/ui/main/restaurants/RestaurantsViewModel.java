package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.persival.go4lunch.utils.ConversionUtils.getHaversineDistance;
import static com.persival.go4lunch.utils.ConversionUtils.getPictureUrl;
import static com.persival.go4lunch.utils.ConversionUtils.getRating;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.data.location.LocationRepository;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.places.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {

    public static final int ANIMATION_STATUS = 1;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private final MediatorLiveData<Integer> gpsMessageLiveData = new MediatorLiveData<>();
    private final LiveData<List<RestaurantsViewState>> restaurantsLiveData;

    @Inject
    public RestaurantsViewModel(
        @NonNull GooglePlacesRepository googlePlacesRepository,
        @NonNull PermissionChecker permissionChecker,
        @NonNull LocationRepository locationRepository
    ) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        gpsMessageLiveData.addSource(locationLiveData, location ->
            combine(location, hasGpsPermissionLiveData.getValue())
        );

        gpsMessageLiveData.addSource(hasGpsPermissionLiveData, hasGpsPermission ->
            combine(locationLiveData.getValue(), hasGpsPermission)
        );

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
                        restaurantsList.add(
                            new RestaurantsViewState(
                                restaurant.getId(),
                                restaurant.getName(),
                                restaurant.getAddress(),
                                getOpeningTime(restaurant.getOpeningHours()),
                                getHaversineDistance(
                                    restaurant.getLatitude(),
                                    restaurant.getLongitude(),
                                    locationLiveData.getValue()
                                ),
                                "(2)",
                                getRating(restaurant.getRating()),
                                getPictureUrl(restaurant.getPhotos())
                            )
                        );
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

    private void combine(@Nullable Location location, @Nullable Boolean hasGpsPermission) {
        if (location == null) {
            if (hasGpsPermission == null || !hasGpsPermission) {
                // Showed in the view if gps is disabled
                gpsMessageLiveData.setValue(R.drawable.baseline_location_off_24);
            } else {
                // Showed in the view if gps is in progress
                gpsMessageLiveData.setValue(ANIMATION_STATUS);
            }
        }
    }

    public LiveData<List<RestaurantsViewState>> getSortedRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public LiveData<Integer> getGpsMessageLiveData() {
        return gpsMessageLiveData;
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        boolean hasGpsPermission = permissionChecker.hasLocationPermission();
        hasGpsPermissionLiveData.setValue(hasGpsPermission);

        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    private String getOpeningTime(NearbyRestaurantsResponse.OpeningHours openingHours) {
        if (openingHours != null && openingHours.isOpenNow()) {
            return "Open";
        } else {
            return "Closed";
        }
    }
}


