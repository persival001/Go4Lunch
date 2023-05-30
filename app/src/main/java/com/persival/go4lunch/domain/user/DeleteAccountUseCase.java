package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;

import javax.inject.Inject;

public class DeleteAccountUseCase {
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public DeleteAccountUseCase(
        @NonNull UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public void invoke() {
        userRepository.deleteAccount();
    }
}
