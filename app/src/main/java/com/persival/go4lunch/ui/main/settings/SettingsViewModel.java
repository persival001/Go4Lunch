package com.persival.go4lunch.ui.main.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.user.DeleteAccountUseCase;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.SetNewUserNameUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<SettingsViewState> settingsViewStateLiveData = new MutableLiveData<>();
    private final SetNewUserNameUseCase setNewUserNameUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @Inject
    public SettingsViewModel(
        GetLoggedUserUseCase getLoggedUserUseCase,
        SetNewUserNameUseCase setNewUserNameUseCase,
        DeleteAccountUseCase deleteAccountUseCase) {
        this.setNewUserNameUseCase = setNewUserNameUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;

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

