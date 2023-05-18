package com.persival.go4lunch.ui.main.maps;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.location.LocationEntity;
import com.persival.go4lunch.data.location.LocationRepository;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.places.GooglePlacesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    private final LocationRepository locationRepository;
    private final PermissionChecker permissionChecker;
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
    private final LiveData<List<NearbyRestaurantsResponse.Place>> nearbyRestaurantsLiveData;

    @Inject
    public MapsViewModel(
        @NonNull LocationRepository locationRepository,
        @NonNull GooglePlacesRepository googlePlacesRepository,
        @NonNull PermissionChecker permissionChecker
    ) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;

        LiveData<LocationEntity> locationLiveData = locationRepository.getLocationLiveData();

        nearbyRestaurantsLiveData = Transformations.switchMap(
            locationLiveData,
            location -> {
                String locationAsString = location.getLatitude() + "," + location.getLongitude();
                return googlePlacesRepository.getNearbyRestaurants(
                    locationAsString,
                    5000,
                    "restaurant",
                    MAPS_API_KEY
                );
            }
        );
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants() {
        return nearbyRestaurantsLiveData;
    }

    public boolean hasLocationPermission() {
        return permissionChecker.hasLocationPermission();
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
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
}



