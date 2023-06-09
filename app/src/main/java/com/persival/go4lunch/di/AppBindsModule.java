package com.persival.go4lunch.di;

import com.persival.go4lunch.data.firebase.FirebaseRepository;
import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.data.permissions.PermissionRepository;
import com.persival.go4lunch.data.places.GooglePlacesRepository;
import com.persival.go4lunch.domain.location.LocationRepository;
import com.persival.go4lunch.domain.permissions.GpsPermissionRepository;
import com.persival.go4lunch.domain.restaurant.PlacesRepository;
import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppBindsModule {

    @Singleton
    @Binds
    public abstract LoggedUserRepository bindLoggedUserRepository(FirebaseRepository implementation);

    @Singleton
    @Binds
    public abstract UserRepository bindUserRepository(FirestoreRepository implementation);

    @Singleton
    @Binds
    public abstract LocationRepository bindLocationRepository(LocationDataRepository implementation);

    @Singleton
    @Binds
    public abstract GpsPermissionRepository bindGpsPermissionRepository(PermissionRepository implementation);

    @Singleton
    @Binds
    public abstract PlacesRepository bindPlacesRepository(GooglePlacesRepository implementation);
}
