package com.persival.go4lunch.domain.restaurant.model;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.OpeningHours;

public class RestaurantEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String address;
    private final float distance;
    @NonNull
    private final OpeningHours openingHours;
    private final int interestedWorkmatesCount;
    private final int rating;
    private final String photoUrl;

    public RestaurantEntity(
        @NonNull String id,
        @NonNull String name,
        @NonNull String address,
        float distance,
        @NonNull OpeningHours openingHours,
        int interestedWorkmatesCount,
        int rating, String photoUrl
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.openingHours = openingHours;
        this.interestedWorkmatesCount = interestedWorkmatesCount;
        this.rating = rating;
        this.photoUrl = photoUrl;
    }
}
