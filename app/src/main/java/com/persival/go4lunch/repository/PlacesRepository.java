package com.persival.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;
import com.persival.go4lunch.BuildConfig;
import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.NearbySearchResponse;
import com.persival.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepository {

    private final GooglePlacesApi googlePlacesApi;

    public PlacesRepository(GooglePlacesApi googlePlacesApi) {
        this.googlePlacesApi = googlePlacesApi;
    }

    public LiveData<List<Restaurant>> getNearbyRestaurants(String location, int radius, int maxResults) {
        MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

        String apiKey = BuildConfig.MAPS_API_KEY;
        String type = "restaurant";
        String rankBy = "distance";

        googlePlacesApi.getNearbyPlaces(location, radius, type, apiKey, rankBy, maxResults).enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    NearbySearchResponse nearbySearchResponse = response.body();
                    if (nearbySearchResponse != null) {
                        List<Place> places = nearbySearchResponse.getResults();
                        List<Restaurant> restaurants = new ArrayList<>();
                        for (Place place : places) {
                            restaurants.add(new Restaurant(place.getId(), place.getName(), place.getAddress()));
                        }
                        restaurantsLiveData.setValue(restaurants);
                        Log.d("API_RESPONSE", "Raw response: " + response.raw().toString());
                        Log.d("API_RESPONSE", "Response body: " + nearbySearchResponse.toString());
                        for (Place place : places) {
                            Log.d("API_RESPONSE", "Place: " + place.toString());
                        }
                    } else {
                        Log.e("API_RESPONSE", "Response body is null");
                    }
                } else {
                    Log.e("API_RESPONSE", "API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "API call failed", t);
            }
        });

        return restaurantsLiveData;
    }
}


