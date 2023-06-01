package com.persival.go4lunch.data.permissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.location.GpsPermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PermissionRepository implements GpsPermissionRepository {

    private final MutableLiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>();

    private final Application context;

    @Inject
    public PermissionRepository(Application context) {
        this.context = context;
    }


    public LiveData<Boolean> getLocationPermission() {
        return locationPermissionLiveData;
    }

    @Override
    public void refreshLocationPermission() {
        boolean hasPermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
        locationPermissionLiveData.setValue(hasPermission);
    }

    @Override
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    public void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

}

