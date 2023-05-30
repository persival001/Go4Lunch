package com.persival.go4lunch.domain.user;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.List;

public interface UserRepository {
    @Nullable
    LoggedUserEntity getLoggedUser();

    @Nullable
    LiveData<List<WorkmateEntity>> getWorkmatesLiveData();

    void setNewUserName(
        String newUserName
    );

    void deleteAccount();
}