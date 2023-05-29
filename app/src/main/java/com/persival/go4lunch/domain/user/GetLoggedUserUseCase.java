package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

public class GetLoggedUserUseCase {

    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetLoggedUserUseCase(
        @NonNull UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Nullable
    public LoggedUserEntity invoke() {
        return userRepository.getLoggedUser();
    }
}
