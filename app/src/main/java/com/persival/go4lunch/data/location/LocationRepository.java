package com.persival.go4lunch.data.location;

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
import com.persival.go4lunch.data.shared_prefs.SharedPreferencesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;
    @NonNull
    private final SharedPreferencesRepository sharedPreferencesRepository;

    @NonNull
    private final MutableLiveData<LocationEntity> locationMutableLiveData = new MutableLiveData<>(null);
    private LocationCallback callback;

    @Inject
    public LocationRepository(
        @NonNull FusedLocationProviderClient fusedLocationProviderClient,
        @NonNull SharedPreferencesRepository sharedPreferencesRepository
    ) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    public LiveData<LocationEntity> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location == null) {

                        // Get last known location
                        LocationEntity savedLocation = sharedPreferencesRepository.getLocation();

                        // If not found, then set to Nancy
                        if (savedLocation == null) {
                            savedLocation = new LocationEntity(
                                48.6921,
                                6.1844
                            );
                        }

                        LocationEntity lastKnownLocation = new LocationEntity(
                            savedLocation.getLatitude(),
                            savedLocation.getLongitude()
                        );
                        locationMutableLiveData.setValue(lastKnownLocation);

                    } else {
                        // Save actual location
                        sharedPreferencesRepository.saveLocation(
                            new LocationEntity(
                                location.getLatitude(),
                                location.getLongitude()
                            )
                        );
                        locationMutableLiveData.setValue(
                            new LocationEntity(
                                location.getLatitude(),
                                location.getLongitude()
                            )
                        );
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

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }
}
