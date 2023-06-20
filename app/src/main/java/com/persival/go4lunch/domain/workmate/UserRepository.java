package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;
import com.persival.go4lunch.domain.workmate.model.UserEntity;

import java.util.List;

public interface UserRepository {

    void setNewUserName(@NonNull String userId, @NonNull String newUserName);

    void deleteAccount();

    void createUser(UserEntity newUser);

    void updateWorkmateInformation(
        LoggedUserEntity user,
        String eatingRestaurantId,
        String eatingRestaurantName
    );

    void updateLikedRestaurants(LoggedUserEntity invoke, boolean isAdded, String restaurantId);

    LiveData<List<UserEatAtRestaurantEntity>> getWorkmatesEatAtRestaurantLiveData();

    LiveData<List<String>> getLikedRestaurantsForUser(String userId);

    LiveData<String> getRestaurantIdForCurrentUser();
}