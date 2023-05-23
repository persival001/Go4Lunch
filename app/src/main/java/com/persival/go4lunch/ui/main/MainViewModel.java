package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(@NonNull final GetLoggedUserUseCase getLoggedUserUseCase) {
        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();

        if (loggedUserEntity != null) {
            MainViewState mainViewState = new MainViewState(
                loggedUserEntity.getId(),
                loggedUserEntity.getName(),
                loggedUserEntity.getEmailAddress(),
                loggedUserEntity.getAvatarPictureUrl()
            );
            mainViewStateLiveData.setValue(mainViewState);
        }
    }

    public LiveData<MainViewState> getAuthenticatedUserLiveData() {
        return mainViewStateLiveData;
    }
}
