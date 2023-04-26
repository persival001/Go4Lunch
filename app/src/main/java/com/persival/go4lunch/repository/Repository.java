package com.persival.go4lunch.repository;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.NearbySearchResponse;
import com.persival.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/";
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>(new ArrayList<>());
    private GooglePlacesApi googlePlacesApi;

    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        googlePlacesApi = retrofit.create(GooglePlacesApi.class);
    }

    public Call<NearbySearchResponse> getNearbyRestaurants(String location, int radius, String apiKey) {
        return googlePlacesApi.getNearbyRestaurants(location, radius, "restaurant", MAPS_API_KEY);
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }
}
