package com.persival.go4lunch.ui.main.workmates;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.workmate.ReadWorkmatesUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

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
        @NonNull ReadWorkmatesUseCase readWorkmatesUseCase,
        Resources resources) {
        viewStateLiveData = Transformations.map(readWorkmatesUseCase.invoke(), users -> {

            List<WorkmatesViewState> workmatesViewState = new ArrayList<>();

            for (WorkmateEntity workmateEntity : users) {
                workmatesViewState.add(
                    new WorkmatesViewState(
                        workmateEntity.getId(),
                        workmateEntity.getPictureUrl(),
                        //getFormattedName(workmateEntity.getWorkmateName(), workmateEntity.getRestaurantName()),
                        "test",
                        //workmateEntity.getRestaurantName()
                        "test"
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