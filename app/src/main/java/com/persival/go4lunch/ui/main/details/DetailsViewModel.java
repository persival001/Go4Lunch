package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.persival.go4lunch.utils.ConversionUtils.getPictureUrl;
import static com.persival.go4lunch.utils.ConversionUtils.getRating;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.places.GooglePlacesRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    private final GooglePlacesRepository googlePlacesRepository;

    @Inject
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
                getPictureUrl(restaurant.getPhotos()),
                restaurant.getName(),
                getRating(restaurant.getRating()),
                restaurant.getAddress(),
                restaurant.getPhoneNumber(),
                restaurant.getWebsite(),
                true,
                "Ginette",
                "https://picsum.photos/200"
            )
        );
    }
}

