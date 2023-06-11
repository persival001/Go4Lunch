package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.restaurant.model.UserRestaurantRelationsEntity;
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
        String eatingRestaurantId,
        String eatingRestaurantName
    );

    LiveData<List<WorkmateEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData();

    void updateLikedRestaurants(LoggedUserEntity invoke, boolean isAdded, String restaurantId);

    LiveData<List<String>> getLikedRestaurantsForUser(String userId);

    LiveData<UserRestaurantRelationsEntity> getRestaurantChosenToEat(String userId);
}