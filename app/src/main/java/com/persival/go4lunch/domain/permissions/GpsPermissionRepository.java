package com.persival.go4lunch.domain.permissions;

import androidx.lifecycle.LiveData;

public interface GpsPermissionRepository {

    void refreshLocationPermission();

    void refreshGpsActivation();

    LiveData<Boolean> isLocationPermission();

    LiveData<Boolean> isGpsActivated();
}