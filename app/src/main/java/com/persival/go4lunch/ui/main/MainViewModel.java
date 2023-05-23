package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final FirestoreRepository firestoreRepository;
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public MainViewModel(
        @NonNull final FirestoreRepository firestoreRepository,
        @NonNull final GetLoggedUserUseCase getLoggedUserUseCase) {
        this.firestoreRepository = firestoreRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LiveData<MainViewState> getAuthenticatedUserLiveData() {
        return Transformations.map(
            firestoreRepository.getAuthenticatedUser(),

            mainViewState -> new MainViewState(
                mainViewState.getId(),
                mainViewState.getName(),
                mainViewState.getEmailAddress(),
                mainViewState.getAvatarPictureUrl() != null ? mainViewState.getAvatarPictureUrl() : ""
            )
        );
    }
}
