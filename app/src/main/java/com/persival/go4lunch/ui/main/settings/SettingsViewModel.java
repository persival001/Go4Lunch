package com.persival.go4lunch.ui.main.settings;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.UserDto;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;

    @Inject
    public SettingsViewModel(
        @NonNull final FirestoreRepository firestoreRepository
    ) {
        this.firestoreRepository = firestoreRepository;
    }

    public LiveData<SettingsViewState> getFirestoreUserViewStateLiveData() {
        return Transformations.map(
            firestoreRepository.getAuthenticatedUser(),

            user -> new SettingsViewState(
                user.getuId(),
                user.getAvatarPictureUrl() != null ? user.getAvatarPictureUrl() : "",
                user.getName(),
                user.getEmailAddress()
            )
        );
    }

    public void setFirestoreUser(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uId = firebaseUser.getUid();
            String avatarPictureUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "";
            UserDto userDto = new UserDto(uId, username, avatarPictureUrl, false, false);
            firestoreRepository.setFirestoreUser(userDto);

            // Update the auth user profile

            String currentName = firebaseUser.getDisplayName();

            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (currentName == null || !currentName.equals(username)) {
                profileUpdatesBuilder.setDisplayName(username);
            }

            UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });
        }
    }

    public void updateUserProfile(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

            firebaseUser.updateProfile(profileUpdates);
        }
    }

}
