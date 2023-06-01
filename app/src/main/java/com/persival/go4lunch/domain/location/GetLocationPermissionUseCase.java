package com.persival.go4lunch.domain.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

public class GetLocationPermissionUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public GetLocationPermissionUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public LiveData<Boolean> invoke() {
        return gpsPermissionRepository.getLocationPermission();
    }
}
