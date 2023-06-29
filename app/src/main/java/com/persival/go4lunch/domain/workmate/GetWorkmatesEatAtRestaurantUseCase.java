package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class GetWorkmatesEatAtRestaurantUseCase {
    @NonNull
    private final UserRepository userRepository;

    @Inject
    public GetWorkmatesEatAtRestaurantUseCase(
        @NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // The workmates who have chosen a restaurant will be at the beginning of the list.
    public LiveData<List<UserEatAtRestaurantEntity>> invoke() {
        LiveData<List<UserEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = userRepository.getWorkmatesEatAtRestaurantLiveData();
        MediatorLiveData<List<UserEatAtRestaurantEntity>> sortedLiveData = new MediatorLiveData<>();

        sortedLiveData.addSource(workmatesEatAtRestaurantLiveData, workmates -> {
            Collections.sort(workmates, (workmate1, workmate2) -> {
                if (workmate1.getRestaurantName() == null && workmate2.getRestaurantName() == null) {
                    return 0;
                }
                if (workmate1.getRestaurantName() == null) {
                    return 1;
                }
                if (workmate2.getRestaurantName() == null) {
                    return -1;
                }
                return 0;
            });
            sortedLiveData.setValue(workmates);
        });

        return sortedLiveData;
    }

}



