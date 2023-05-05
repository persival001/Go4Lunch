package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.persival.go4lunch.utils.ConversionUtils.getOpeningTime;
import static com.persival.go4lunch.utils.ConversionUtils.getPictureUrl;
import static com.persival.go4lunch.utils.ConversionUtils.getRating;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.repository.LocationRepository;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.repository.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsViewModel extends ViewModel {

    private final GooglePlacesRepository googlePlacesRepository;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    private final MutableLiveData<Boolean> locationPermissionRequested = new MutableLiveData<>(false);


    public RestaurantsViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository,
        @NonNull PermissionChecker permissionChecker,
        @NonNull LocationRepository locationRepository
    ) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.googlePlacesRepository = googlePlacesRepository;

        locationRepository.getLocationLiveData().observeForever(newLocation -> {
            refresh();
        });

    }

    public LiveData<List<RestaurantsViewState>> getRestaurantsLiveData() {
        return Transformations.map(
            googlePlacesRepository.getNearbyRestaurants(
                locationRepository.getGpsCoordinatesAsString(),
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
                        "200",
                        "(2)",
                        getRating(restaurant.getRating()),
                        getPictureUrl(restaurant.getPhotos())
                    ));
                }
                return restaurantsList;
            });
    }


    public LiveData<Boolean> getLocationPermissionRequested() {
        return locationPermissionRequested;
    }

    public void requestLocationPermission() {
        locationPermissionRequested.setValue(true);
    }

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        boolean hasGpsPermission = permissionChecker.hasLocationPermission();
        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        }
    }

    public void refresh() {
        startLocationRequest();
    }

}

