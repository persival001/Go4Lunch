package com.persival.go4lunch.domain.location;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.persival.go4lunch.data.location.LocationDataRepository;

import javax.inject.Inject;

public class RefreshLocationUseCase {

    @NonNull
    private final LocationRepository locationRepository;

    @Inject
    public RefreshLocationUseCase(
        @NonNull LocationDataRepository locationDataRepository) {
        this.locationRepository = locationDataRepository;
    }

    @SuppressLint("MissingPermission")
    public void invoke() {
       /* boolean hasGpsPermission = permissionChecker.hasLocationPermission();
        hasGpsPermissionLiveData.setValue(hasGpsPermission);

        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }*/
    }
}
