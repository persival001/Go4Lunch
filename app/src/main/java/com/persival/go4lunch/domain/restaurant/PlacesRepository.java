package com.persival.go4lunch.domain.restaurant;

import androidx.lifecycle.LiveData;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;

import java.util.List;

public interface PlacesRepository {

    LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants(
        String locationAsString,
        int radius,
        String restaurant,
        String mapsApiKey
    );

    LiveData<NearbyRestaurantsResponse.Place> getRestaurantLiveData(
        String restaurantId,
        String apiKey
    );

}
