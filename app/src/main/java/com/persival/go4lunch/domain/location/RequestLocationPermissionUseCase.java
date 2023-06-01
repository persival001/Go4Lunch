package com.persival.go4lunch.domain.location;

import android.app.Activity;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class RequestLocationPermissionUseCase {

    @NonNull
    private final GpsPermissionRepository gpsPermissionRepository;

    @Inject
    public RequestLocationPermissionUseCase(
        @NonNull GpsPermissionRepository gpsPermissionRepository) {
        this.gpsPermissionRepository = gpsPermissionRepository;
    }

    public void invoke(Activity activity, int requestCode) {
        gpsPermissionRepository.requestLocationPermission(activity, requestCode);
    }
}
