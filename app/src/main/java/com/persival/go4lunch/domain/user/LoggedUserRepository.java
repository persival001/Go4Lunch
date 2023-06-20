package com.persival.go4lunch.domain.user;

import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

public interface LoggedUserRepository {

    void setNewUserName(String newUserName);

    void deleteAccount();

    LoggedUserEntity getLoggedUser();

    LiveData<LoggedUserEntity> getUserNameChangedLiveData();
}