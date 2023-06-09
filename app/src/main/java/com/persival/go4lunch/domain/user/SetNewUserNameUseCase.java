package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class SetNewUserNameUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final LoggedUserRepository loggedUserRepository;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public SetNewUserNameUseCase(
        @NonNull UserRepository userRepository,
        @NonNull LoggedUserRepository loggedUserRepository,
        @NonNull FirebaseAuth firebaseAuth
    ) {
        this.userRepository = userRepository;
        this.loggedUserRepository = loggedUserRepository;
        this.firebaseAuth = firebaseAuth;
    }

    public void invoke(String newUserName) {
        if (firebaseAuth.getCurrentUser() != null) {
            userRepository.setNewUserName(firebaseAuth.getCurrentUser().getUid(), newUserName);
            loggedUserRepository.setNewUserName(newUserName);
        }
    }
}

