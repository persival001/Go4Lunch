package com.persival.go4lunch.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.SetNewUserNameUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.DeleteAccountUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<SettingsViewState> settingsViewStateLiveData = new MutableLiveData<>();
    @NonNull
    private final SetNewUserNameUseCase setNewUserNameUseCase;
    @NonNull
    private final DeleteAccountUseCase deleteAccountUseCase;

    @Inject
    public SettingsViewModel(
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull SetNewUserNameUseCase setNewUserNameUseCase,
        @NonNull DeleteAccountUseCase deleteAccountUseCase
    ) {
        this.setNewUserNameUseCase = setNewUserNameUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;

        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();

        if (loggedUserEntity != null) {
            SettingsViewState settingsViewState = new SettingsViewState(
                loggedUserEntity.getId(),
                loggedUserEntity.getName(),
                loggedUserEntity.getEmailAddress(),
                loggedUserEntity.getPictureUrl() != null ? loggedUserEntity.getPictureUrl() : ""
            );
            settingsViewStateLiveData.setValue(settingsViewState);
        }
    }

    public LiveData<SettingsViewState> getLoggedUserLiveData() {
        return settingsViewStateLiveData;
    }

    public void setNewUserName(String newUserName) {
        setNewUserNameUseCase.invoke(newUserName);
    }

    public void deleteAccount() {
        deleteAccountUseCase.invoke();
    }
}

