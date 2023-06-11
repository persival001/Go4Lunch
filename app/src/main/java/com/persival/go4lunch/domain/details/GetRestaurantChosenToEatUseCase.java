package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.restaurant.model.UserRestaurantRelationsEntity;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class GetRestaurantChosenToEatUseCase {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;

    @Inject
    public GetRestaurantChosenToEatUseCase(
        @NonNull UserRepository userRepository,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.userRepository = userRepository;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
    }

    public LiveData<Boolean> invoke(String restaurantIdDetail) {
        LiveData<UserRestaurantRelationsEntity> userEatsAtRestaurantLiveData
            = userRepository.getRestaurantChosenToEat(getLoggedUserUseCase.invoke().getId());

        return Transformations.map(userEatsAtRestaurantLiveData, userRestaurantRelationsEntity -> {
            if (userRestaurantRelationsEntity != null) {
                return restaurantIdDetail.equals(userRestaurantRelationsEntity.getRestaurantId());
            } else {
                return false;
            }
        });
    }

}



