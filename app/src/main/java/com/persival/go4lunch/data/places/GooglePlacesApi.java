package com.persival.go4lunch.data.places;

import com.persival.go4lunch.data.places.model.AutocompletePredictionResponse;
import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.places.model.PlaceDetailsResponse;

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

    @GET("maps/api/place/details/json")
    Call<PlaceDetailsResponse> getPlaceDetail(
        @Query("place_id") String placeId,
        @Query("key") String apiKey
    );

    @GET("maps/api/place/autocomplete/json")
    Call<AutocompletePredictionResponse> getAutocomplete(
        @Query("input") String input,
        @Query("radius") int radius,
        @Query("key") String apiKey
    );
}

