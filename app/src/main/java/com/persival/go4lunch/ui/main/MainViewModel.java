package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.domain.autocomplete.AutocompleteEntity;
import com.persival.go4lunch.domain.autocomplete.GetAutocompletesUseCase;
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
    @NonNull
    private final GetAutocompletesUseCase getAutocompletesUseCase;
    private final MutableLiveData<MainViewStateAutocomplete> selectedAutocompleteItem = new MutableLiveData<>();

    @Inject
    public MainViewModel(
        @NonNull GetRestaurantIdForCurrentUserUseCase getRestaurantIdForCurrentUserUseCase,
        @NonNull GetUserNameChangedUseCase getUserNameChangedUseCase,
        @NonNull GetNearbyRestaurantsUseCase getNearbyRestaurantsUseCase,
        @NonNull GetAutocompletesUseCase getAutocompletesUseCase
    ) {
        this.getRestaurantIdForCurrentUserUseCase = getRestaurantIdForCurrentUserUseCase;
        this.getNearbyRestaurantsUseCase = getNearbyRestaurantsUseCase;
        this.getAutocompletesUseCase = getAutocompletesUseCase;

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

    public LiveData<List<MainViewStateAutocomplete>> getAutocompletesLiveData() {
        return Transformations.switchMap(searchStringLiveData, searchString -> {
            if (searchString.length() >= 2) {
                return Transformations.map(getAutocompletesUseCase.invoke(searchString), autocompleteEntities -> {
                    List<MainViewStateAutocomplete> autocompletes = new ArrayList<>();

                    for (AutocompleteEntity autocompleteEntity : autocompleteEntities) {
                        autocompletes.add(
                            new MainViewStateAutocomplete(
                                autocompleteEntity.getPlaceId(),
                                autocompleteEntity.getName()
                            )
                        );
                    }

                    return autocompletes;
                });
            } else {
                return new MutableLiveData<>(new ArrayList<>());
            }
        });
    }

    public LiveData<NearbyRestaurantsEntity> getSelectedRestaurantLiveData() {
        return Transformations.switchMap(selectedAutocompleteItem, selectedAutocomplete -> {
            if (selectedAutocomplete != null) {
                return Transformations.switchMap(getNearbyRestaurantsUseCase.invoke(), restaurants -> {
                    for (NearbyRestaurantsEntity restaurant : restaurants) {
                        if (restaurant.getId().equals(selectedAutocomplete.getPlaceId())) {
                            return new MutableLiveData<>(restaurant);
                        }
                    }
                    return new MutableLiveData<>(null);
                });
            }
            return new MutableLiveData<>(null);
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

    public void onAutocompleteSelected(MainViewStateAutocomplete mainViewStateAutocomplete) {
        selectedAutocompleteItem.setValue(mainViewStateAutocomplete);
    }

}

