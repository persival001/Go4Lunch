package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class SetNewUserNameUseCase {
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public SetNewUserNameUseCase(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(String newUserName) {
        userRepository.setNewUserName(newUserName);
    }
}

