package com.persival.go4lunch.Data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static GooglePlacesApi createGooglePlacesApi() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit.create(GooglePlacesApi.class);
    }
}