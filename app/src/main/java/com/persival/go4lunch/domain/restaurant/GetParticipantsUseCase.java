package com.persival.go4lunch.domain.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.workmate.UserRepository;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetParticipantsUseCase {
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetParticipantsUseCase(
        @NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<HashMap<String, Integer>> invoke() {
        LiveData<List<WorkmateEatAtRestaurantEntity>> workmatesLiveData = userRepository.getWorkmatesEatAtRestaurantLiveData();

        return Transformations.map(workmatesLiveData, workmates -> {
            HashMap<String, Integer> restaurantUserCount = new HashMap<>();
            for (WorkmateEatAtRestaurantEntity workmate : workmates) {
                String restaurantId = workmate.getRestaurantId();
                Integer count = restaurantUserCount.get(restaurantId);
                if (count == null) {
                    count = 0;
                }
                restaurantUserCount.put(restaurantId, count + 1);
            }
            return restaurantUserCount;
        });
    }
}
