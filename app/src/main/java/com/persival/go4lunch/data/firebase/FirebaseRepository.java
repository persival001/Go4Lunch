package com.persival.go4lunch.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.persival.go4lunch.domain.user.LoggedUserRepository;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import java.util.Objects;

import javax.inject.Inject;

public class FirebaseRepository implements LoggedUserRepository {
    @NonNull
    private static final String USERS = "users";
    @NonNull
    private static final String USER_EAT_AT_RESTAURANT = "userEatAtRestaurant";
    @NonNull
    private static final String TAG = "FirestoreRepository";
    @NonNull
    private final FirebaseAuth firebaseAuth;
    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final MutableLiveData<LoggedUserEntity> loggedUserEntityMutableLiveData = new MutableLiveData<>();

    @Inject
    public FirebaseRepository(
        @NonNull FirebaseAuth firebaseAuth,
        @NonNull FirebaseFirestore firebaseFirestore
    ) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
        firebaseAuth.addAuthStateListener(firebaseAuth1 -> {
            FirebaseUser firebaseUser = getCurrentUser();
            if (firebaseUser != null &&
                firebaseUser.getDisplayName() != null &&
                firebaseUser.getEmail() != null
            ) {
                LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
                    firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getPhotoUrl() == null ? null : Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString(),
                    firebaseUser.getEmail());
                loggedUserEntityMutableLiveData.setValue(loggedUserEntity);
            }
        });
    }

    // ----- Get a logged user -----
    private FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // ----- Get a listened logged user -----
    public LiveData<LoggedUserEntity> getUserNameChangedLiveData() {
        return loggedUserEntityMutableLiveData;
    }

    // ----- Get a logged user -----
    public LoggedUserEntity getLoggedUser() {
        FirebaseUser firebaseUser = getCurrentUser();

        if (firebaseUser == null || firebaseUser.getDisplayName() == null || firebaseUser.getEmail() == null) {
            return null;
        } else {
            return new LoggedUserEntity(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),
                firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString(),
                firebaseUser.getEmail()
            );
        }
    }

    // ----- Change user name -----
    public void setNewUserName(@NonNull String newUserName) {
        FirebaseUser firebaseUser = getCurrentUser();

        if (firebaseUser != null && !Objects.equals(firebaseUser.getDisplayName(), newUserName)) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUserName)
                .build();

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseUser.getEmail() != null) {
                        LoggedUserEntity loggedUserEntity = new LoggedUserEntity(
                            firebaseUser.getUid(),
                            newUserName,
                            firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString(),
                            firebaseUser.getEmail()
                        );
                        loggedUserEntityMutableLiveData.setValue(loggedUserEntity);
                    } else {
                        Log.e(TAG, "Firebase Auth: Error updating user name", task.getException());
                    }
                });
        }
    }

    // ----- Delete account -----
    public void deleteAccount() {
        FirebaseUser firebaseUser = getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore.collection(USERS)
                .document(firebaseUser.getUid())
                .delete();
            firebaseFirestore.collection(USER_EAT_AT_RESTAURANT)
                .document(firebaseUser.getUid())
                .delete();
            firebaseUser.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    } else {
                        Log.e(TAG, "Error deleting user account", task.getException());
                    }
                });
        }
        firebaseAuth.signOut();
    }
}