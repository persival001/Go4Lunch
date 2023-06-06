package com.persival.go4lunch.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.persival.go4lunch.domain.user.LoggedUserRepository;

import java.util.Objects;

import javax.inject.Inject;

public class FirebaseRepository implements LoggedUserRepository {
    @NonNull
    private static final String TAG = "FirestoreRepository";
    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseRepository(
        @NonNull FirebaseAuth firebaseAuth
    ) {
        this.firebaseAuth = firebaseAuth;
    }

    // ----- Change user name -----
    public void setNewUserName(String newUserName) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && !Objects.equals(firebaseUser.getDisplayName(), newUserName)) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUserName)
                .build();

            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Firebase Auth: Error updating user name", task.getException());
                }
            });
        }
    }

    // ----- Delete account -----
    public void deleteAccount() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseAuth.signOut();
                } else {
                    Log.e(TAG, "Firebase Auth: Error deleting user", task.getException());
                }
            });
        }
    }

}
