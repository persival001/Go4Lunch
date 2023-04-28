package com.persival.go4lunch.ui.mainactivity.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.Data.model.RestaurantEntity;
import com.persival.go4lunch.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsViewModel extends ViewModel {

    private final MutableLiveData<List<RestaurantsViewState>> restaurantsLiveData = new MutableLiveData<>();
    private final Repository repository;

    public RestaurantsViewModel(
        @NonNull final Repository repository
    ) {
        this.repository = repository;
    }

    public LiveData<List<RestaurantsViewState>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public void fetchRestaurants(String location, int radius) {
        repository.getNearbyRestaurants(location, radius, "restaurant", MAPS_API_KEY);
        repository.getPlacesLiveData().observeForever(places -> {
            // Transform NearbySearchResponse.Place list to RestaurantsViewState list
            List<RestaurantsViewState> restaurants = new ArrayList<>();
            for (RestaurantEntity.Place place : places) {
                // Assuming RestaurantsViewState has a constructor that takes a NearbySearchResponse.Place object
                restaurants.add(new RestaurantsViewState(place));
            }
            restaurantsLiveData.setValue(restaurants);
        });
    }
}

