package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.AuthenticatedUser;
import com.persival.go4lunch.data.firestore.FirestoreRepository;

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

    public LiveData<AuthenticatedUser> getAuthenticatedUserLiveData() {
        return Transformations.map(
            firestoreRepository.getAuthenticatedUser(),

            user -> new AuthenticatedUser(
                user.getuId(),
                user.getName(),
                user.getEmailAddress(),
                user.getAvatarPictureUrl() != null ? user.getAvatarPictureUrl() : ""
            )
        );
    }
}
