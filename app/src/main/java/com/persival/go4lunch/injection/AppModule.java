package com.persival.go4lunch.injection;

import android.app.Application;

import com.google.android.gms.location.LocationServices;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.repository.GooglePlacesRepository;
import com.persival.go4lunch.data.repository.LocationRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    // This class is not meant to be instantiated
    private AppModule() {
    }

    @Provides
    public static PermissionChecker providePermissionChecker(Application application) {
        return new PermissionChecker(application);
    }

    @Provides
    public static LocationRepository provideLocationRepository(Application application) {
        return new LocationRepository(LocationServices.getFusedLocationProviderClient(application), application);
    }

    @Provides
    public static GooglePlacesRepository provideGooglePlacesRepository() {
        return new GooglePlacesRepository();
    }
}
