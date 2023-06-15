package com.persival.go4lunch.data.places.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetailsResponse {
    @Nullable
    @SerializedName("result")
    private NearbyRestaurantsResponse.Place result;

    @Nullable
    @SerializedName("place_id")
    private String placeId;

    @Nullable
    @SerializedName("name")
    private String name;

    @Nullable
    @SerializedName("vicinity")
    private String address;

    @Nullable
    @SerializedName("website")
    private String website;

    @Nullable
    @SerializedName("formatted_phone_number")
    private String phoneNumber;

    @Nullable
    @SerializedName("photos")
    private List<NearbyRestaurantsResponse.Photo> photos;

    @Nullable
    @SerializedName("rating")
    private Float rating;

    @Nullable
    public NearbyRestaurantsResponse.Place getResult() {
        return result;
    }
}


