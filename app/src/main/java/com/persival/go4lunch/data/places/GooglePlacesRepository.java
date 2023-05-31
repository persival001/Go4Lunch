package com.persival.go4lunch.data.places;

import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.data.GooglePlacesApi;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.model.PlaceDetailsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class GooglePlacesRepository {

    private final GooglePlacesApi googlePlacesApi;

    private final Map<String, NearbyRestaurantsResponse.Place> placeDetailCache = new HashMap<>();

    @Inject
    public GooglePlacesRepository(GooglePlacesApi googlePlacesApi) {
        this.googlePlacesApi = googlePlacesApi;
    }

    public LiveData<List<NearbyRestaurantsResponse.Place>> getNearbyRestaurants(String location, int radius, String type, String apiKey) {
        MutableLiveData<List<NearbyRestaurantsResponse.Place>> restaurantsLiveData = new MutableLiveData<>();

        googlePlacesApi.getNearbyPlaces(location, radius, type, apiKey).enqueue(new Callback<NearbyRestaurantsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbyRestaurantsResponse> call,
                                   @NonNull Response<NearbyRestaurantsResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    restaurantsLiveData.setValue(response.body().getResults());
                    Log.d("RESPONSE", "The server responds to requests");
                } else {
                    Log.d("NO RESPONSE", "The server does not respond to requests");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyRestaurantsResponse> call, @NonNull Throwable t) {
                Log.d("FAILURE", "Server failure");
            }
        });

        return restaurantsLiveData;
    }

    public LiveData<NearbyRestaurantsResponse.Place> getRestaurantLiveData(String restaurantId, String apiKey) {
        MutableLiveData<NearbyRestaurantsResponse.Place> restaurantLiveData = new MutableLiveData<>();

        NearbyRestaurantsResponse.Place cached = placeDetailCache.get(restaurantId);

        if (cached == null) {
            googlePlacesApi.getPlaceDetail(restaurantId, apiKey).enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(
                    @NonNull Call<PlaceDetailsResponse> call,
                    @NonNull Response<PlaceDetailsResponse> response
                ) {
                    if (response.isSuccessful() && response.body() != null) {
                        placeDetailCache.put(restaurantId, response.body().getResult());
                        restaurantLiveData.setValue(response.body().getResult());
                        Log.d("RESPONSE", "RESPONSE OK RESTO ID");
                    } else {
                        Log.d("NO RESPONSE", "The server does not respond to requests");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                    Log.d("FAILURE", "Server failure");
                }
            });
        } else {
            restaurantLiveData.setValue(cached);
        }

        return restaurantLiveData;
    }
}
