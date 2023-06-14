package com.persival.go4lunch.domain.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.persival.go4lunch.domain.workmate.UserRepository;

import javax.inject.Inject;

public class GetParticipantsUseCase {

    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetParticipantsUseCase(
        @NonNull UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public LiveData<String> invoke(String restaurantIdToCompare) {
        return Transformations.map(userRepository.getAllRestaurantIds(), restaurantIds -> {
            int count = 0;
            if (restaurantIdToCompare != null) {
                for (String restaurantId : restaurantIds) {
                    if (restaurantId.equals(restaurantIdToCompare)) {
                        count++;
                    }
                }
            }
            return String.valueOf(count);
        });
    }
}
