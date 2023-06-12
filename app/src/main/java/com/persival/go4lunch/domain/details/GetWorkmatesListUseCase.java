package com.persival.go4lunch.domain.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetWorkmatesListUseCase {
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final IsRestaurantChosenToEatUseCase isRestaurantChosenToEatUseCase;
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetWorkmatesListUseCase(
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull IsRestaurantChosenToEatUseCase isRestaurantChosenToEatUseCase, @NonNull UserRepository userRepository
    ) {
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.isRestaurantChosenToEatUseCase = isRestaurantChosenToEatUseCase;
        this.userRepository = userRepository;
    }

    public LiveData<List<WorkmateEntity>> invoke(String restaurantId) {
        LoggedUserEntity currentUser = getLoggedUserUseCase.invoke();

        if (currentUser == null) {
            return new MutableLiveData<>(null);
        } else {
            LiveData<List<WorkmateEntity>> workmatesLiveData = userRepository.getWorkmatesLiveData();

            return Transformations.map(workmatesLiveData, workmates -> {
                List<WorkmateEntity> filteredWorkmates = new ArrayList<>();
                for (WorkmateEntity workmate : workmates) {
                    // restaurantId.equals(userRepository.getRestaurantChosenToEat(restaurantId).getValue())
                    if (1 == 1) {
                        filteredWorkmates.add(workmate);
                    }
                }
                return filteredWorkmates;
            });
        }
    }


}
