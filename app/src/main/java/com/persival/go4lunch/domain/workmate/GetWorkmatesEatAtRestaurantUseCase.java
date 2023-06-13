package com.persival.go4lunch.domain.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;

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

    // Sort the list of workmates by restaurant name.
    // If the restaurant name is null, the workmate will be at the end of the list
    public LiveData<List<WorkmateEatAtRestaurantEntity>> invoke() {
        LiveData<List<WorkmateEatAtRestaurantEntity>> workmatesEatAtRestaurantLiveData = userRepository.getWorkmatesEatAtRestaurantLiveData();
        MediatorLiveData<List<WorkmateEatAtRestaurantEntity>> sortedLiveData = new MediatorLiveData<>();

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



