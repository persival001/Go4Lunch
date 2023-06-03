package com.persival.go4lunch.ui.main.workmates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.workmate.GetWorkmatesUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewState>> viewStateLiveData;
    private final Application context;

    @Inject
    public WorkmatesViewModel(
        @NonNull GetWorkmatesUseCase getWorkmatesUseCase,
        Application context) {
        viewStateLiveData = Transformations.map(getWorkmatesUseCase.invoke(), users -> {

            List<WorkmatesViewState> workmatesViewState = new ArrayList<>();

            for (WorkmateEntity workmateEntity : users) {
                workmatesViewState.add(
                    new WorkmatesViewState(
                        workmateEntity.getId(),
                        workmateEntity.getWorkmatePictureUrl(),
                        getFormattedName(workmateEntity.getWorkmateName(), workmateEntity.getRestaurantName()),
                        workmateEntity.getRestaurantName()
                    )
                );
            }

            return workmatesViewState;
        });
        this.context = context;
    }

    // Add "is eating at" before the workmate name and add the restaurant name
    private String getFormattedName(String name, String restaurantName) {
        if (name != null && restaurantName != null) {
            return name + context.getString(R.string.is_eating_at) + restaurantName;
        } else {
            return "";
        }
    }

    public LiveData<List<WorkmatesViewState>> getViewStateLiveData() {
        return viewStateLiveData;
    }
}