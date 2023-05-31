package com.persival.go4lunch.data.shared_prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.persival.go4lunch.domain.location.model.LocationEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesRepository {

    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesRepository(Application context) {
        sharedPreferences = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
    }

    @Nullable
    public LocationEntity getLocation() {
        float latitude = sharedPreferences.getFloat(KEY_LATITUDE, -1);
        float longitude = sharedPreferences.getFloat(KEY_LONGITUDE, -1);

        if (latitude != -1 && longitude != -1) {
            return new LocationEntity(
                latitude,
                longitude
            );
        }

        return null;
    }

    public void saveLocation(LocationEntity location) {
        sharedPreferences.edit()
            .putFloat(KEY_LATITUDE, (float) location.getLatitude())
            .putFloat(KEY_LONGITUDE, (float) location.getLongitude())
            .apply();
    }
}
