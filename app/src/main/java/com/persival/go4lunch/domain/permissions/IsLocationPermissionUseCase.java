package com.persival.go4lunch.domain.permissions;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

public class IsLocationPermissionUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public IsLocationPermissionUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public LiveData<Boolean> invoke() {
        return gpsPermissionRepository.isLocationPermission();
    }
}

