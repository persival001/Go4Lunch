package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
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
        FirebaseUser firebaseUser = userRepository.getFirebaseUser();
        if (firebaseUser == null || firebaseUser.getDisplayName() == null || firebaseUser.getEmail() == null) {
            return null;
        } else {
            return new LoggedUserEntity(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString()
            );
        }
    }

}
