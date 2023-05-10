package com.persival.go4lunch.ui.main.maps;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.location.LocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapsViewModel extends ViewModel {
    private final LocationRepository locationRepository;
    private final PermissionChecker permissionChecker;

    @Inject
    public MapsViewModel(@NonNull LocationRepository locationRepository, @NonNull PermissionChecker permissionChecker) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
    }

    public boolean hasLocationPermission() {
        return permissionChecker.hasLocationPermission();
    }

    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }
}



