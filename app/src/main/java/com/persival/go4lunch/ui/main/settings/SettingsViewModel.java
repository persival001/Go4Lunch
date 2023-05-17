package com.persival.go4lunch.ui.main.settings;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.data.firestore.User;

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
            firestoreRepository.getFirestoreUser(),

            user -> new SettingsViewState(
                user.getuId() != null ? user.getuId() : "",
                user.getAvatarPictureUrl() != null ? user.getAvatarPictureUrl() : "",
                user.getName() != null ? user.getName() : "",
                user.getEmailAddress() != null ? user.getEmailAddress() : ""
            )
        );
    }

    public void setFirestoreUser(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uId = firebaseUser.getUid();
            String name = username;
            String eMailAddress = firebaseUser.getEmail();
            String avatarPictureUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;

            User user = new User(uId, name, eMailAddress, avatarPictureUrl);
            firestoreRepository.setFirestoreUser(user);
        }
    }

    public void updateUserProfile(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // The user display name has been successfully updated
                    } else {
                        // An error occurred while updating the user display name
                    }
                });
        }
    }


}
