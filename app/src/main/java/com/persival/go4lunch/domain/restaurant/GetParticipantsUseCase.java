package com.persival.go4lunch.domain.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;

import java.util.List;

import javax.inject.Inject;

public class GetParticipantsUseCase {

    @NonNull
    private final GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase;

    @Inject
    public GetParticipantsUseCase(
        @NonNull GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase
    ) {
        this.getWorkmatesEatAtRestaurantUseCase = getWorkmatesEatAtRestaurantUseCase;
    }

    public LiveData<String> invoke(String restaurantId) {
        LiveData<List<WorkmateEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData =
            getWorkmatesEatAtRestaurantUseCase.invoke();

        return Transformations.map(workmatesEatAtRestaurantLiveData, workmates -> {
            int count = 0;
            for (WorkmateEatAtRestaurantEntity workmate : workmates) {
                if (workmate.getRestaurantId() != null && workmate.getRestaurantId().equals(restaurantId)) {
                    count++;
                }
            }
            return String.valueOf(count);
        });
    }

}
