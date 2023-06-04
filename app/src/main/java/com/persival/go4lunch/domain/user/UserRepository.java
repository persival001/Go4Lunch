package com.persival.go4lunch.domain.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.List;

public interface UserRepository {

    @NonNull
    LiveData<List<WorkmateEntity>> getWorkmatesLiveData();

    void setNewUserName(
        String newUserName
    );

    void deleteAccount();
}