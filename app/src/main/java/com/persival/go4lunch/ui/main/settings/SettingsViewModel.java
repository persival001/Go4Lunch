package com.persival.go4lunch.ui.main.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<SettingsViewState> settingsViewStateLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(
        GetLoggedUserUseCase getLoggedUserUseCase
    ) {

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

    /*public void setNewUserName(String username) {
        setNewUserNameUseCase.setNewUserName(username);
    }

    public void deleteAccount() {
        deleteAccountUseCase.deleteAccount();
    }
*/
}
