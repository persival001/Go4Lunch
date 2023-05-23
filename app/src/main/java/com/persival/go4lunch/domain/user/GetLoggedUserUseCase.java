package com.persival.go4lunch.domain.user;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

public class GetLoggedUserUseCase {
    private final FirebaseAuth firebaseAuth;

    @Inject
    public GetLoggedUserUseCase(
        FirebaseAuth firebaseAuth
    ) {
        this.firebaseAuth = firebaseAuth;
    }

    @Nullable
    public LoggedUserEntity invoke() {
        FirebaseUser firebaseAuthUser = firebaseAuth.getCurrentUser();

        if (firebaseAuthUser == null || firebaseAuthUser.getDisplayName() == null) {
            return null;
        } else {
            return new LoggedUserEntity(
                firebaseAuthUser.getUid(),
                firebaseAuthUser.getDisplayName(),
                firebaseAuthUser.getEmail(),
                firebaseAuthUser.getPhotoUrl() == null ? null : firebaseAuthUser.getPhotoUrl().toString()
            );
        }
    }
}
