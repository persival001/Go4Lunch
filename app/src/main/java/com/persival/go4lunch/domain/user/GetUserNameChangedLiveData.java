package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

public class GetUserNameChangedLiveData {

    @NonNull
    private final LoggedUserRepository loggedUserRepository;

    @Inject
    public GetUserNameChangedLiveData(
        @NonNull LoggedUserRepository loggedUserRepository
    ) {
        this.loggedUserRepository = loggedUserRepository;
    }

    public LiveData<LoggedUserEntity> invoke() {
        return loggedUserRepository.getUserNameChangedLiveData();
    }

}

