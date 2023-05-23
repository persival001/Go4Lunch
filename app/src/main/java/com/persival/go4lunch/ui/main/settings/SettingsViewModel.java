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
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public SettingsViewModel(
        @NonNull final FirestoreRepository firestoreRepository,
        GetLoggedUserUseCase getLoggedUserUseCase) {
        this.firestoreRepository = firestoreRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LiveData<SettingsViewState> getFirestoreUserViewStateLiveData() {
        return Transformations.map(
            firestoreRepository.getAuthenticatedUser(),

            user -> new SettingsViewState(
                user.getId(),
                user.getName(),
                user.getEmailAddress(),
                user.getAvatarPictureUrl() != null ? user.getAvatarPictureUrl() : ""
            )
        );
    }

    public LoggedUserEntity getLoggedUser() {
        return getLoggedUserUseCase.invoke();
    }

    public void setFirestoreUser(String username) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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
                    firestoreRepository.setFirestoreUser(getLoggedUser());
                    Log.d(TAG, "User profile updated.");
                }
            });
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
