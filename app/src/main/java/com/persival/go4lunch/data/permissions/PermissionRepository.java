package com.persival.go4lunch.data.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class PermissionRepository implements GpsPermissionRepository {

    private final MutableLiveData<Boolean> locationPermissionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGpsActivatedLiveData = new MutableLiveData<>();
    private final Context context;

    @Inject
    public PermissionRepository(
        @NonNull @ApplicationContext Context context
    ) {
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
        boolean hasPermission = ContextCompat.checkSelfPermission(
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

