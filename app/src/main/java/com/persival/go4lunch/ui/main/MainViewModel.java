package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.restaurant.GetRestaurantIdForCurrentUserUseCase;
import com.persival.go4lunch.domain.user.GetUserNameChangedLiveData;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final LiveData<MainViewState> mainViewStateLiveData;
    @NonNull
    private final GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase;

    @Inject
    public MainViewModel(
        @NonNull GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase,
        @NonNull GetUserNameChangedLiveData getUserNameChangedLiveData
    ) {
        this.getRestaurantIdForCurrentUserUseCase = getRestaurantIdForCurrentUserUseCase;

        mainViewStateLiveData = Transformations.map(getUserNameChangedLiveData.invoke(), this::mapToMainViewState);
    }

    private MainViewState mapToMainViewState(LoggedUserEntity loggedUserEntity) {
        if (loggedUserEntity == null) {
            return null;
        }
        return new MainViewState(
            loggedUserEntity.getId(),
            loggedUserEntity.getName(),
            loggedUserEntity.getEmailAddress(),
            loggedUserEntity.getPictureUrl()
        );
    }

    public LiveData<MainViewState> getAuthenticatedUserLiveData() {
        return mainViewStateLiveData;
    }

    public LiveData<String> getRestaurantIdForCurrentUserLiveData() {
        return getRestaurantIdForCurrentUserUseCase.invoke();
    }
}

