package com.persival.go4lunch.domain.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class SaveLoggedUserUseCase {
    private final FirebaseAuth firebaseAuth;
    private final Context context;

    @Inject
    public SaveLoggedUserUseCase(
        FirebaseAuth firebaseAuth,
        Context context) {
        this.firebaseAuth = firebaseAuth;
        this.context = context;
    }
    
    public void invoke() {
        FirebaseUser firebaseAuthUser = firebaseAuth.getCurrentUser();

        if (firebaseAuthUser != null && firebaseAuthUser.getDisplayName() != null) {
            // Get SharedPreferences instance
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

            // Use the SharedPreferences editor to save the user data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userId", firebaseAuthUser.getUid());
            editor.putString("userName", firebaseAuthUser.getDisplayName());
            editor.putString("userPhotoUrl", firebaseAuthUser.getPhotoUrl() != null ? firebaseAuthUser.getPhotoUrl().toString() : null);
            editor.apply();
        }
    }
}
