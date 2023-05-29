package com.persival.go4lunch.domain.user;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class SetNewUserNameUseCase {
    private final FirebaseAuth firebaseAuth;

    @Inject
    public SetNewUserNameUseCase(
        FirebaseAuth firebaseAuth
    ) {
        this.firebaseAuth = firebaseAuth;
    }

   /* @Nullable
    public SetNewUserNameUseCase invoke() {
    }*/

}
