package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class IsRestaurantChosenToEatUseCase {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public IsRestaurantChosenToEatUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LiveData<Boolean> invoke(String restaurantIdDetail) {
        LiveData<String> userEatsAtRestaurantLiveData = userRepository.getRestaurantChosenToEat(getLoggedUserUseCase.invoke().getId());

        return Transformations.map(userEatsAtRestaurantLiveData, userRestaurantRelationsEntity -> {
            if (userRestaurantRelationsEntity != null) {
                return restaurantIdDetail.equals(userEatsAtRestaurantLiveData.getValue());
            } else {
                return false;
            }
        });
    }

}



