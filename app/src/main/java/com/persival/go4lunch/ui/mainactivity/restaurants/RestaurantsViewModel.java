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

    private final Repository repository;
    private final MutableLiveData<List<RestaurantsViewState>> restaurantsLiveData = new MutableLiveData<>();

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
        repository.getRestaurantsLiveData().observeForever(restaurants -> {
            List<RestaurantsViewState> restaurantsList = new ArrayList<>();
            for (RestaurantEntity.Place restaurant : restaurants) {
                restaurantsList.add(new RestaurantsViewState(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getTypeOfCuisineAndAddress(),
                    restaurant.getOpeningTime(),
                    "200",
                    "(2)",
                    restaurant.getRating(),
                    restaurant.getPictureUrl()
                ));
            }
            restaurantsLiveData.setValue(restaurantsList);
        });
    }

}

