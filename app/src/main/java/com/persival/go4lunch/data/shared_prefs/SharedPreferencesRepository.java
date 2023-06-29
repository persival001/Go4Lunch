package com.persival.go4lunch.data.shared_prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.persival.go4lunch.domain.location.model.LocationEntity;
import com.persival.go4lunch.domain.notifications.PreferencesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesRepository implements PreferencesRepository {

    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    public static final String KEY_WORK_ID = "workId";
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

    @Nullable
    public String getWorkId() {
        return sharedPreferences.getString(KEY_WORK_ID, null);
    }

    public void saveWorkId(String workId) {
        sharedPreferences.edit()
            .putString(KEY_WORK_ID, workId)
            .apply();
    }

    public void removeWorkId() {
        sharedPreferences.edit().remove(KEY_WORK_ID).apply();
    }

}
