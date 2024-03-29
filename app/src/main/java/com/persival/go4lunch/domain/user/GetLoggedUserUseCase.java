package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

public class GetLoggedUserUseCase {

    @NonNull
    private final LoggedUserRepository loggedUserRepository;

    @Inject
    public GetLoggedUserUseCase(
        @NonNull LoggedUserRepository loggedUserRepository
    ) {
        this.loggedUserRepository = loggedUserRepository;
    }

    @Nullable
    public LoggedUserEntity invoke() {
        return loggedUserRepository.getLoggedUser();
    }

}
