package com.persival.go4lunch.ui.mainactivity.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.Data.NearbySearchResponse;
import com.persival.go4lunch.model.Restaurant;
import com.persival.go4lunch.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void fetchRestaurants(String location, int radius, String apiKey) {
        repository.getNearbyRestaurants(location, radius, MAPS_API_KEY).enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NearbySearchResponse.Place> places = response.body().getRestaurants();

                    // Limiter le nombre de restaurants à 10
                    List<NearbySearchResponse.Place> limitedPlaces = new ArrayList<>();
                    for (int i = 0; i < 10 && i < places.size(); i++) {
                        limitedPlaces.add(places.get(i));
                    }

                    List<RestaurantsViewState> restaurantViewStates
                        = convertRestaurantsToViewStates(limitedPlaces);
                    restaurantsLiveData.setValue(restaurantViewStates);
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                Log.d("RestaurantsViewModel", "onFailure: " + t.getMessage());
            }
        });
    }


    private List<RestaurantsViewState> convertRestaurantsToViewStates(List<NearbySearchResponse.Place> places) {
        List<RestaurantsViewState> restaurantViewStates = new ArrayList<>();
        for (NearbySearchResponse.Place place : places) {
            // Conversion d'un objet Place en un objet Restaurant
            Restaurant restaurant = new Restaurant(
                place.getId(), // Utiliser le hashCode de place_id comme ID
                place.getName(),
                place.getAddress(),
                place.getName(),
                place.getName(),
                place.getName(),
                place.getName()
                // Récupérez l'URL de la photo à partir de l'objet Photo
                // et des autres informations nécessaires pour construire l'objet Restaurant
            );

            // Conversion d'un objet Restaurant en un objet RestaurantsViewState
            RestaurantsViewState viewState = new RestaurantsViewState(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhotoUrl(),
                restaurant.getOpeningHours(),
                restaurant.getDistance(),
                restaurant.getWorkmates()
            );
            restaurantViewStates.add(viewState);
        }
        return restaurantViewStates;
    }
}