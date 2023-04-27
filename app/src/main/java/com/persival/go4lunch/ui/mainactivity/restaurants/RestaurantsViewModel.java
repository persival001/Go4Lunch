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

    public void fetchRestaurants(String location, int radius, int maxResults) {
        repository.getNearbyRestaurants(location, radius, "restaurant", MAPS_API_KEY, "distance", maxResults)
            .enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RestaurantsViewState> restaurantViewStates
                            = convertRestaurantsToViewStates(response.body().getRestaurants());
                        restaurantsLiveData.setValue(restaurantViewStates);
                        Log.d("API_RESPONSE", "Raw response: " + response.raw().toString());
                        Log.d("API_RESPONSE", "Response body: " + response.body().toString());

                    } else {
                        // Gérer l'échec de la requête ici
                        Log.d("API_RESPONSE", "Failure: " + response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                    // Gérer l'échec de la requête ici
                    Log.d("API_RESPONSE", "Error: " + t.getMessage());
                }
            });
    }

    private List<RestaurantsViewState> convertRestaurantsToViewStates(List<NearbySearchResponse.Place> places) {
        List<RestaurantsViewState> restaurantViewStates = new ArrayList<>();
        for (NearbySearchResponse.Place place : places) {
            Log.d("API_RESPONSE", "Place: " + place.toString());
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
        Log.d("CONVERTED_DATA", "RestaurantsViewState: " + restaurantViewStates.toString());

        return restaurantViewStates;
    }
}