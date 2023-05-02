package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.repository.GooglePlacesRepository;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.functions.Function1;

public class RestaurantsViewModel extends ViewModel {

    private final GooglePlacesRepository googlePlacesRepository;

    public RestaurantsViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
    }

    public LiveData<List<RestaurantsViewState>> getRestaurantsLiveData() {
        return Transformations.map(googlePlacesRepository.getNearbyRestaurants("48.6921,6.1844", 500, "restaurant", MAPS_API_KEY), new Function1<List<NearbyRestaurantsResponse.Place>, List<RestaurantsViewState>>() {
            @Override
            public List<RestaurantsViewState> invoke(List<NearbyRestaurantsResponse.Place> places) {
                List<RestaurantsViewState> restaurantsList = new ArrayList<>();
                for (NearbyRestaurantsResponse.Place restaurant : places) {
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
                return restaurantsList;
            }
        });
    }
}

