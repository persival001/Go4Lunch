package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class SetLikedRestaurantUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public SetLikedRestaurantUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public void invoke(boolean isAdded, String restaurantId) {
        LoggedUserEntity user = getLoggedUserUseCase.invoke();
        if (user != null) {
            userRepository.updateLikedRestaurants(
                user,
                isAdded,
                restaurantId
            );
        }
    }
}
