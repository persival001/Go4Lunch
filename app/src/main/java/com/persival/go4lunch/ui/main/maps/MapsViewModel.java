package com.persival.go4lunch.ui.main.maps;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.repository.LocationRepository;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;

public class MapsViewModel extends ViewModel {

    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    public MapsViewModel(
        @NonNull LocationRepository locationRepository,
        @NonNull PermissionChecker permissionChecker
    ) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        locationRepository.startLocationRequest();
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

