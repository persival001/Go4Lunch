package com.persival.go4lunch.ui.main.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private final MutableLiveData<SettingsViewState> settingsViewStateLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(
        @NonNull final FirestoreRepository firestoreRepository,
        GetLoggedUserUseCase getLoggedUserUseCase) {
        this.firestoreRepository = firestoreRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;

        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();

        if (loggedUserEntity != null) {
            SettingsViewState settingsViewState = new SettingsViewState(
                loggedUserEntity.getId(),
                loggedUserEntity.getName(),
                loggedUserEntity.getEmailAddress(),
                loggedUserEntity.getAvatarPictureUrl()
            );
            settingsViewStateLiveData.setValue(settingsViewState);
        }
    }

    public LiveData<SettingsViewState> getAuthenticatedUserLiveData() {
        return settingsViewStateLiveData;
    }

    /*public void setFirestoreUser(String username) {
        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (loggedUserEntity != null) {
            String currentName = loggedUserEntity.getName();
            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (!currentName.equals(username)) {
                profileUpdatesBuilder.setDisplayName(username);
            }

            UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firestoreRepository.setFirestoreUser(LoggedUserDto);
                        Log.d(TAG, "User profile updated.");
                    }
                });
        }
    }*/

    public void updateUserProfile(String username) {
        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();

        if (loggedUserEntity != null) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

            firebaseUser.updateProfile(profileUpdates);
        }
    }

}
