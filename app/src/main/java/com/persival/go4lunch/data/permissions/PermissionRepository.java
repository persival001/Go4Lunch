package com.persival.go4lunch.data.permissions;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PermissionRepository implements GpsPermissionRepository {

    private final MutableLiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGpsActivatedLiveData = new MutableLiveData<>();
    private final Application context;

    @Inject
    public PermissionRepository(Application context) {
        this.context = context;
    }


    public LiveData<Boolean> isLocationPermission() {
        return locationPermissionLiveData;
    }

    public LiveData<Boolean> isGpsActivated() {
        return isGpsActivatedLiveData;
    }

    @Override
    public void refreshLocationPermission() {
        boolean hasPermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
        locationPermissionLiveData.setValue(hasPermission);
    }

    public void refreshGpsActivation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isGpsActivatedLiveData.setValue(isGPSEnabled);
        } else {
            isGpsActivatedLiveData.setValue(false);
        }
    }

}

