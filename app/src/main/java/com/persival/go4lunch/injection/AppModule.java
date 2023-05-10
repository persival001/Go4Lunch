package com.persival.go4lunch.injection;

import android.app.Application;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.persival.go4lunch.data.GooglePlacesApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/";

    // This class is not meant to be instantiated
    private AppModule() {
    }

    @Singleton
    @Provides
    public static FusedLocationProviderClient provideFusedLocationProviderClient(Application application) {
        return LocationServices.getFusedLocationProviderClient(application);
    }

    @Singleton
    @Provides
    public static GooglePlacesApi provideGooglePlacesApi() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit.create(GooglePlacesApi.class);
    }
}
