package com.persival.go4lunch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.data.permissions.PermissionRepository;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Application sApplication;

    @Inject
    PermissionRepository permissionRepository;
    LocationDataRepository locationDataRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        permissionRepository.refreshLocationPermission();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}