package com.persival.go4lunch.ui.main.workmates;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewState>> viewStateLiveData;
    @NonNull
    private final Resources resources;

    @Inject
    public WorkmatesViewModel(
        @NonNull GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase,
        @NonNull Resources resources,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase
    ) {
        this.resources = resources;

        LoggedUserEntity loggedUserEntity = getLoggedUserUseCase.invoke();
        String loggedUserId = loggedUserEntity != null ? loggedUserEntity.getId() : null;

        viewStateLiveData = Transformations.map(getWorkmatesEatAtRestaurantUseCase.invoke(), users -> {

            List<WorkmatesViewState> workmatesViewState = new ArrayList<>();

            for (WorkmateEatAtRestaurantEntity workmateEatAtRestaurantEntity : users) {
                if (workmateEatAtRestaurantEntity != null &&
                    loggedUserId != null &&
                    !workmateEatAtRestaurantEntity.getId().equals(loggedUserId)) {
                    workmatesViewState.add(
                        new WorkmatesViewState(
                            workmateEatAtRestaurantEntity.getId(),
                            workmateEatAtRestaurantEntity.getPictureUrl() != null ? workmateEatAtRestaurantEntity.getPictureUrl() : "",
                            getFormattedName(
                                workmateEatAtRestaurantEntity.getName(),
                                workmateEatAtRestaurantEntity.getRestaurantName()
                            )
                        )
                    );
                }
            }

            return workmatesViewState;
        });

    }

    // Add "is eating at" before the workmate name and add the restaurant name
    private String getFormattedName(String name, String restaurantName) {
        if (name != null && restaurantName != null) {
            return resources.getString(R.string.is_eating_at, name, restaurantName);
        } else {
            return resources.getString(R.string.hasnt_decided_yet, name);
        }
    }

    public LiveData<List<WorkmatesViewState>> getViewStateLiveData() {
        return viewStateLiveData;
    }
}