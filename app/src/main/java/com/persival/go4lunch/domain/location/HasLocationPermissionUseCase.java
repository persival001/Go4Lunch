package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;

import com.persival.go4lunch.data.permissions.PermissionRepository;

import javax.inject.Inject;

public class HasLocationPermissionUseCase {

    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;

    @Inject
    public HasLocationPermissionUseCase(
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;
    }

    public boolean invoke() {
        boolean hasGpsPermission = permissionRepository.hasLocationPermission();

        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
        return hasGpsPermission;
    }
}
