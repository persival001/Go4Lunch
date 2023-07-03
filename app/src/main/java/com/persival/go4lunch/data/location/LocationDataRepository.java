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
import com.google.android.gms.location.Priority;
import com.persival.go4lunch.data.shared_prefs.SharedPreferencesRepository;
import com.persival.go4lunch.domain.location.LocationRepository;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationDataRepository implements LocationRepository {
    private static final int SMALLEST_DISPLACEMENT_THRESHOLD_METER = 250;
    private static final long INTERVAL = 10000;
    private static final long FASTEST_INTERVAL = INTERVAL / 2;
    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;
    @NonNull
    private final SharedPreferencesRepository sharedPreferencesRepository;
    @NonNull
    private final MutableLiveData<LocationEntity> locationMutableLiveData = new MutableLiveData<>();
    private LocationCallback callback;

    @Inject
    public LocationDataRepository(
        @NonNull FusedLocationProviderClient fusedLocationProviderClient,
        @NonNull SharedPreferencesRepository sharedPreferencesRepository
    ) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    @Override
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

                        // If not found, then set to Paris
                        if (savedLocation == null) {
                            savedLocation = new LocationEntity(
                                48.8566,
                                2.3522
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

        LocationRequest locationRequest = new LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, INTERVAL
        )
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
            .setMinUpdateDistanceMeters(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
            .build();

        fusedLocationProviderClient.removeLocationUpdates(callback);

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
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
