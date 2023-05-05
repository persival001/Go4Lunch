package com.persival.go4lunch.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

public class LocationRepository {

    @NonNull
    private final Context context;
    @NonNull
    private SharedPreferences sharedPreferences;
    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;
    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);
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
        Task<Location> location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location gpsLocation = task.getResult();
                if (gpsLocation == null) {
                    Log.d("LOCATION", "Location is null ");
                    sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
                    double latitude = sharedPreferences.getFloat("latitude", 0);
                    double longitude = sharedPreferences.getFloat("longitude", 0);
                    Log.d("LOCATION SHARED", "Location shared " + gpsLocation.getLatitude() + " " + gpsLocation.getLongitude());

                    // If default values are (0,0) then set to Nancy
                    if (latitude == 0 && longitude == 0) {
                        gpsLocation = new Location("Server");
                        gpsLocation.setLatitude(48.6921);
                        gpsLocation.setLongitude(6.1844);
                        Log.d("LOCATION (0,0)", "Location shared 0,0" + gpsLocation.getLatitude() + " " + gpsLocation.getLongitude());

                    }

                    Location lastKnownLocation = new Location("Server");
                    lastKnownLocation.setLatitude(latitude);
                    lastKnownLocation.setLongitude(longitude);
                    Log.d("LOCATION LASTKNOWN", "Location LASTKNOWN " + gpsLocation.getLatitude() + " " + gpsLocation.getLongitude());
                    locationMutableLiveData.setValue(gpsLocation);
                }
                saveLastKnownLocation(gpsLocation.getLatitude(), gpsLocation.getLongitude());
                Log.d("LOCATION SUCCESS", "Location shared Success" + gpsLocation.getLatitude() + " " + gpsLocation.getLongitude());
                locationMutableLiveData.setValue(gpsLocation);
            }
        });
    }

    private void saveLastKnownLocation(double latitude, double longitude) {
        sharedPreferences = context.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("longitude", (float) longitude);
        editor.apply();
    }


    public String getGpsCoordinatesAsString() {
        Location location = locationMutableLiveData.getValue();
        if (location == null) {
            return null;
        }
        return location.getLatitude() + "," + location.getLongitude();
    }


    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }
}
