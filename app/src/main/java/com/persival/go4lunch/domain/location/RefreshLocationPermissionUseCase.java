package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class RefreshLocationPermissionUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public RefreshLocationPermissionUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public void invoke() {
        gpsPermissionRepository.refreshLocationPermission();
    }
}
