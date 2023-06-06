package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class DeleteAccountUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final LoggedUserRepository loggedUserRepository;

    @Inject
    public DeleteAccountUseCase(
        @NonNull UserRepository userRepository,
        @NonNull LoggedUserRepository loggedUserRepository) {
        this.userRepository = userRepository;
        this.loggedUserRepository = loggedUserRepository;
    }

    public void invoke() {
        userRepository.deleteAccount();
        loggedUserRepository.deleteAccount();
    }
}
