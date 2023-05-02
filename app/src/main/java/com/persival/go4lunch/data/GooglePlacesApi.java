package com.persival.go4lunch.data;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbyRestaurantsResponse> getNearbyPlaces(
        @Query("location") String location,
        @Query("radius") int radius,
        @Query("type") String type,
        @Query("key") String apiKey
    );

    @GET("maps/api/place/detail/json")
    Call<NearbyRestaurantsResponse.Place> getPlaceDetail(
        @Query("place_id") String placeId,
        @Query("key") String apiKey
    );
}

