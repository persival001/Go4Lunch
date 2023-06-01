package com.persival.go4lunch.domain.location;

import android.app.Activity;

import androidx.lifecycle.LiveData;

public interface GpsPermissionRepository {

    void refreshLocationPermission();

    LiveData<Boolean> getLocationPermission();

    boolean hasLocationPermission();

    void requestLocationPermission(Activity activity, int requestCode);
}