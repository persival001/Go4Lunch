package com.persival.go4lunch.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;
    @NonNull
    private final Context context;
    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;
    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);
    private SharedPreferences sharedPreferences;
    private LocationCallback callback;

    public LocationRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient, @NonNull Context context) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.context = context;
    }

    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location gpsLocation = locationResult.getLastLocation();
                    if (gpsLocation == null) {
                        // Get last known location
                        sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
                        double latitude = sharedPreferences.getFloat("latitude", 0);
                        double longitude = sharedPreferences.getFloat("longitude", 0);

                        // If default values are (0,0) then set to Nancy
                        if (latitude == 0 && longitude == 0) {
                            gpsLocation = new Location("Server");
                            gpsLocation.setLatitude(48.6921);
                            gpsLocation.setLongitude(6.1844);
                        }

                        // Set last known location or Nancy
                        Location lastKnownLocation = new Location("Server");
                        lastKnownLocation.setLatitude(latitude);
                        lastKnownLocation.setLongitude(longitude);
                        locationMutableLiveData.setValue(gpsLocation);
                    } else {
                        // Save actual location
                        saveLastKnownLocation(gpsLocation.getLatitude(), gpsLocation.getLongitude());
                        locationMutableLiveData.setValue(gpsLocation);
                    }
                }
            };
        }

        fusedLocationProviderClient.removeLocationUpdates(callback);
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                .setInterval(LOCATION_REQUEST_INTERVAL_MS),
            callback,
            Looper.getMainLooper()
        );
    }

    private void saveLastKnownLocation(double latitude, double longitude) {
        sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("longitude", (float) longitude);
        editor.apply();
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }
}
