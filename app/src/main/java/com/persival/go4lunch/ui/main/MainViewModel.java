package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.ui.main.settings.SettingsViewState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;

    @Inject
    public MainViewModel(
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
}
