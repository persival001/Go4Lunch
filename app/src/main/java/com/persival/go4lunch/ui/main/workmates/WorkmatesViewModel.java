package com.persival.go4lunch.ui.main.workmates;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEatAtRestaurantEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewState>> viewStateLiveData;
    private final Resources resources;

    @Inject
    public WorkmatesViewModel(
        @NonNull GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase,
        Resources resources) {
        viewStateLiveData = Transformations.map(getWorkmatesEatAtRestaurantUseCase.invoke(), users -> {

            List<WorkmatesViewState> workmatesViewState = new ArrayList<>();

            for (WorkmateEatAtRestaurantEntity workmateEatAtRestaurantEntity : users) {
                workmatesViewState.add(
                    new WorkmatesViewState(
                        workmateEatAtRestaurantEntity.getId(),
                        workmateEatAtRestaurantEntity.getPictureUrl(),
                        getFormattedName(
                            workmateEatAtRestaurantEntity.getName(),
                            workmateEatAtRestaurantEntity.getRestaurantName()
                        )
                    )
                );
            }

            return workmatesViewState;
        });
        this.resources = resources;
    }

    // Add "is eating at" before the workmate name and add the restaurant name
    private String getFormattedName(String name, String restaurantName) {
        if (name != null && restaurantName != null) {
            return name + " " + resources.getString(R.string.is_eating_at) + " " + restaurantName;
        } else {
            return name + " " + resources.getString(R.string.hasnt_decided_yet);
        }
    }

    public LiveData<List<WorkmatesViewState>> getViewStateLiveData() {
        return viewStateLiveData;
    }
}