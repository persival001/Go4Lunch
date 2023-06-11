package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

public class UpdateWorkmateUseCase {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public UpdateWorkmateUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public void invoke(String eatingRestaurantId, String eatingRestaurantName) {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        userRepository.updateWorkmateInformation(currentUser, eatingRestaurantId, eatingRestaurantName);
    }
}
