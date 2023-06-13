package com.persival.go4lunch.domain.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class GetRestaurantIdForCurrentUserUseCase {
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetRestaurantIdForCurrentUserUseCase(
        @NonNull UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public LiveData<String> invoke() {
        return userRepository.getRestaurantIdForCurrentUser();
    }

}
