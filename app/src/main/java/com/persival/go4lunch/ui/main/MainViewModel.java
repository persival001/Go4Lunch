package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.restaurant.GetNearbyRestaurantsUseCase;
import com.persival.go4lunch.domain.restaurant.GetRestaurantIdForCurrentUserUseCase;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.domain.user.GetUserNameChangedUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final LiveData<MainViewState> mainViewStateLiveData;
    private final MutableLiveData<String> searchStringLiveData = new MutableLiveData<>();

    @NonNull
    private final GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase;
    @NonNull
    private final GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase;

    @Inject
    public MainViewModel(
        @NonNull GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase,
        @NonNull GetUserNameChangedUseCase getUserNameChangedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase
    ) {
        this.getRestaurantIdForCurrentUserUseCase = getRestaurantIdForCurrentUserUseCase;
        this.getNearbyRestaurantsUseCase = getNearbyRestaurantsUseCase;

        mainViewStateLiveData = Transformations.map(getUserNameChangedUseCase.invoke(), this::mapToMainViewState);

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

    public LiveData<List<NearbyRestaurantsEntity>> getFilteredRestaurantsLiveData() {
        return Transformations.switchMap(searchStringLiveData, searchString -> {
            if (searchString.length() >= 2) {
                return Transformations.switchMap(getNearbyRestaurantsUseCase.invoke(), restaurants -> {

                    List<NearbyRestaurantsEntity> filteredRestaurants = new ArrayList<>();
                    for (NearbyRestaurantsEntity restaurant : restaurants) {
                        if (restaurant.getName().toLowerCase().contains(searchString.toLowerCase())) {
                            filteredRestaurants.add(restaurant);
                        }
                    }
                    return new MutableLiveData<>(filteredRestaurants);
                });
            } else {
                return new MutableLiveData<>(new ArrayList<>());
            }
        });
    }

    public void updateSearchString(String newSearchString) {
        searchStringLiveData.setValue(newSearchString);
    }

    public LiveData<MainViewState> getAuthenticatedUserLiveData() {
        return mainViewStateLiveData;
    }

    public LiveData<String> getRestaurantIdForCurrentUserLiveData() {
        return getRestaurantIdForCurrentUserUseCase.invoke();
    }
}

