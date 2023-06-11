package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.List;

public interface UserRepository {

    @NonNull
    LiveData<List<WorkmateEntity>> getWorkmatesLiveData();

    void setNewUserName(@NonNull String userId, @NonNull String newUserName);

    void deleteAccount();

    void createUser(WorkmateEntity newUser);

    void updateWorkmateInformation(
        LoggedUserEntity user,
        List<String> newLikedRestaurantIds,
        String eatingRestaurantId,
        String eatingRestaurantName
    );

    LiveData<List<WorkmateEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData();

}