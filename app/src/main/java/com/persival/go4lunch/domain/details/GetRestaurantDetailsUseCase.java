package com.persival.go4lunch.domain.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.restaurant.PlacesRepository;

import javax.inject.Inject;

public class GetRestaurantDetailsUseCase {
    @NonNull
    private final PlacesRepository placesRepository;

    @Inject
    public GetRestaurantDetailsUseCase(
        @NonNull PlacesRepository placesRepository
    ) {
        this.placesRepository = placesRepository;
    }

    public LiveData<NearbyRestaurantsResponse.Place> invoke(@NonNull String restaurantId) {
        return placesRepository.getRestaurantLiveData(
            restaurantId,
            MAPS_API_KEY
        );
    }
}
