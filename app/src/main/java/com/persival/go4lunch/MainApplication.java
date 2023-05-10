package com.persival.go4lunch;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
    }

}