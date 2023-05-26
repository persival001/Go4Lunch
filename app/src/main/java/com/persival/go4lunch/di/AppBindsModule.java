package com.persival.go4lunch.di;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.domain.workmate.GetWorkmateUseCase;

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
    public abstract GetWorkmateUseCase.WorkmateRepository bindWorkmateRepository(FirestoreRepository implementation);
}
