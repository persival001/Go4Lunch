package com.persival.go4lunch.domain.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.domain.restaurant.model.PlaceDetailsEntity;

import java.util.List;

public interface PlacesRepository {

    LiveData<List<NearbyRestaurantsEntity>> getNearbyRestaurants(
        String locationAsString,
        int radius,
        String restaurant,
        String mapsApiKey
    );

    LiveData<PlaceDetailsEntity> getRestaurantLiveData(
        @NonNull String restaurantId,
        @NonNull String apiKey
    );

}
