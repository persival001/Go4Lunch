package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.firestore.FirestoreRepository;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

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
        MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();
        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();

        if (loggedUserEntity != null) {
            MainViewState mainViewState = new MainViewState(
                loggedUserEntity.getId(),
                loggedUserEntity.getName(),
                loggedUserEntity.getEmailAddress(),
                loggedUserEntity.getAvatarPictureUrl() != null ? loggedUserEntity.getAvatarPictureUrl() : ""
            );
            mainViewStateLiveData.setValue(mainViewState);
        }

        return mainViewStateLiveData;
    }

}
