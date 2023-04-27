package com.persival.go4lunch.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbySearchResponse> getNearbyPlaces(
        @Query("location") String location,
        @Query("radius") int radius,
        @Query("type") String type,
        @Query("key") String apiKey,
        @Query("rankby") String rankBy,
        @Query("maxResults") int maxResults
    );
}

