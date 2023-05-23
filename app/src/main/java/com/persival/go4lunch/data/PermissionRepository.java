package com.persival.go4lunch.data;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PermissionRepository {

    private final MutableLiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>();

    private final Application context;

    @Inject
    public PermissionRepository(Application context) {
        this.context = context;
    }

    public LiveData<Boolean> getLocationPermission() {
        return locationPermissionLiveData;
    }

    public void refreshLocationPermission() {
        boolean hasPermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
        locationPermissionLiveData.setValue(hasPermission);
    }

    public void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }
}

