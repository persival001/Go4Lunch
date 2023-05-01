package com.persival.go4lunch.repository;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.libraries.places.api.model.Place;
import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.model.RestaurantEntity;
import com.persival.go4lunch.ui.mainactivity.details.DetailsViewState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/";
    private final MutableLiveData<List<RestaurantEntity.Place>> restaurantsLiveData = new MutableLiveData<>(new ArrayList<>());
    private GooglePlacesApi googlePlacesApi;

    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        googlePlacesApi = retrofit.create(GooglePlacesApi.class);
    }

    public void getNearbyRestaurants(String location, int radius, String type, String apiKey) {
        googlePlacesApi.getNearbyPlaces(location, radius, type, apiKey).enqueue(new Callback<RestaurantEntity>() {
            @Override
            public void onResponse(Call<RestaurantEntity> call, Response<RestaurantEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    restaurantsLiveData.setValue(response.body().getResults());
                    Log.d("REPONSE", "onResponse: " + response.body().getResults().get(0).getName());
                } else {
                    Log.d("NO RESPONSE", " <------------ " + response.body().getResults().get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<RestaurantEntity> call, Throwable t) {
                Log.d("FAILURE", " <------------ ");
            }
        });
    }

    public LiveData<List<RestaurantEntity.Place>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

   public LiveData<RestaurantEntity.Place> getRestaurantLiveData(long restaurantId) {
        return Transformations.map(restaurantsLiveData, restaurants -> {
            for (RestaurantEntity.Place restaurant : restaurants) {
                if (restaurant.getId() == restaurantId) {
                    return restaurant;
                }
            }
            return null;
        });
    }

}
