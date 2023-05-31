package com.persival.go4lunch.ui.main.workmates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.workmate.GetWorkmatesUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WorkmatesViewModel extends ViewModel {

    private final LiveData<List<WorkmatesViewState>> viewStateLiveData;

    @Inject
    public WorkmatesViewModel(
        @NonNull GetWorkmatesUseCase getWorkmatesUseCase
    ) {
        viewStateLiveData = Transformations.map(getWorkmatesUseCase.invoke(), users -> {
            List<WorkmatesViewState> workmatesViewState = new ArrayList<>();

            for (WorkmateEntity workmateEntity : users) {
                workmatesViewState.add(
                    new WorkmatesViewState(
                        workmateEntity.getId(),
                        workmateEntity.getUsername(),
                        workmateEntity.getEmail(),
                        workmateEntity.getPhotoUrl()
                    )
                );
            }

            return workmatesViewState;
        });
    }

    public LiveData<List<WorkmatesViewState>> getViewStateLiveData() {
        return viewStateLiveData;
    }
}