package com.persival.go4lunch.di;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.location.LocationDataRepository;
import com.persival.go4lunch.domain.location.LocationRepository;
import com.persival.go4lunch.domain.user.UserRepository;

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
    public abstract UserRepository bindUserRepository(FirestoreRepository implementation);

    @Singleton
    @Binds
    public abstract LocationRepository bindLocationRepository(LocationDataRepository implementation);
}
