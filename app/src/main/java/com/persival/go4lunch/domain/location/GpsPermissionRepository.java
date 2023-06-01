package com.persival.go4lunch.domain.location;

import androidx.lifecycle.LiveData;

public interface GpsPermissionRepository {

    void refreshLocationPermission();

    LiveData<Boolean> getLocationPermission();

    boolean hasLocationPermission();

    LiveData<Boolean> isGpsActivated();
}