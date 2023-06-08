package com.persival.go4lunch.domain.permissions;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class RefreshGpsActivationUseCase {
    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public RefreshGpsActivationUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public void invoke() {
        gpsPermissionRepository.refreshGpsActivation();
    }
}
