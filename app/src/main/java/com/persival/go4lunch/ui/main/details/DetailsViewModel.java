package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.repository.GooglePlacesRepository;

public class DetailsViewModel extends ViewModel {
    private final GooglePlacesRepository googlePlacesRepository;

    public DetailsViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
    }

    public LiveData<DetailsViewState> getDetailViewStateLiveData(String restaurantId) {
        return Transformations.map(
            googlePlacesRepository.getRestaurantLiveData(restaurantId, MAPS_API_KEY),
            restaurant -> new DetailsViewState(
                restaurant.getId(),
                restaurant.getPictureUrl(),
                restaurant.getName(),
                restaurant.getRating(), // TODO Persival transformer le rating sur 3 ici par exemple
                restaurant.getTypeOfCuisineAndAddress(),
                restaurant.getPhoneNumber(),
                restaurant.getWebsite(),
                true,
                "(2)",
                "https://pravatar.cc/150?img=3"
            )
        );
    }
}

