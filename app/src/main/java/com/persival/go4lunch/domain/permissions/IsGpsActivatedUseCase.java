package com.persival.go4lunch.domain.permissions;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

public class IsGpsActivatedUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public IsGpsActivatedUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public LiveData<Boolean> invoke() {
        return gpsPermissionRepository.isGpsActivated();
    }
}
