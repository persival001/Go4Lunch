package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class SetRestaurantChosenToEatUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public SetRestaurantChosenToEatUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public void invoke(String restaurantId, String restaurantName) {
        userRepository.updateWorkmateInformation(
            getLoggedUserUseCase.invoke(),
            restaurantId,
            restaurantName
        );
    }

}
