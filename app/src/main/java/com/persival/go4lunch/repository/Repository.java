package com.persival.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.NearbySearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/";
    private final MutableLiveData<List<NearbySearchResponse.Place>> placesLiveData = new MutableLiveData<>(new ArrayList<>());
    private GooglePlacesApi googlePlacesApi;

    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        googlePlacesApi = retrofit.create(GooglePlacesApi.class);
    }

    public void getNearbyRestaurants(String location, int radius, String type, String apiKey) {
        googlePlacesApi.getNearbyPlaces(location, radius, type, apiKey).enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    placesLiveData.setValue(response.body().getResults());
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                // Handle the error
            }
        });
    }

    public LiveData<List<NearbySearchResponse.Place>> getPlacesLiveData() {
        return placesLiveData;
    }
}
