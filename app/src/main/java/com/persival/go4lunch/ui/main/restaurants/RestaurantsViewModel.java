package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.persival.go4lunch.utils.ConversionUtils.getHaversineDistance;
import static com.persival.go4lunch.utils.ConversionUtils.getOpeningTime;
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
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.repository.GooglePlacesRepository;
import com.persival.go4lunch.data.repository.LocationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantsViewModel extends ViewModel {

    public static final int ANIMATION_STATUS = 1;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    private final MediatorLiveData<Integer> gpsMessageLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> gpsLocationLiveData = new MutableLiveData<>();
    private final LiveData<List<RestaurantsViewState>> restaurantsLiveData;

    public RestaurantsViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository,
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

        restaurantsLiveData = Transformations.switchMap(gpsLocationLiveData, location -> {
            if (location != null) {
                return Transformations.map(
                    googlePlacesRepository.getNearbyRestaurants(
                        location,
                        5000,
                        "restaurant",
                        MAPS_API_KEY
                    ),
                    places -> {
                        List<RestaurantsViewState> restaurantsList = new ArrayList<>();
                        for (NearbyRestaurantsResponse.Place restaurant : places) {
                            restaurantsList.add(new RestaurantsViewState(
                                restaurant.getId(),
                                restaurant.getName(),
                                restaurant.getAddress(),
                                getOpeningTime(restaurant.getOpeningHours()),
                                getHaversineDistance(restaurant.getLatitude(), restaurant.getLongitude(), gpsLocationLiveData.getValue()),
                                "(2)",
                                getRating(restaurant.getRating()),
                                getPictureUrl(restaurant.getPhotos())
                            ));
                        }
                        return restaurantsList;
                    }
                );
            } else {
                return new MutableLiveData<>(null);
            }
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
        } else {
            // Gps location is available
            gpsLocationLiveData.setValue(location.getLatitude() + "," + location.getLongitude());
        }
    }

    public LiveData<List<RestaurantsViewState>> getSortedRestaurantsLiveData() {
        return Transformations.map(restaurantsLiveData, restaurantsList -> {
            if (restaurantsList == null) return null;

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

    public LiveData<Integer> getGpsMessageLiveData() {
        return gpsMessageLiveData;
    }
}


