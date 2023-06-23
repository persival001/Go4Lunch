package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.LoggedUserRepository;

import javax.inject.Inject;

public class DeleteAccountUseCase {
    @NonNull
    private final LoggedUserRepository loggedUserRepository;

    @Inject
    public DeleteAccountUseCase(
        @NonNull LoggedUserRepository loggedUserRepository
    ) {
        this.loggedUserRepository = loggedUserRepository;
    }

    public void invoke() {
        loggedUserRepository.deleteAccount();
    }
}
