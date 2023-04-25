package com.persival.go4lunch.ui.mainactivity.restaurants;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class RestaurantsViewState {

    @NonNull
    private final String name;

    @NonNull
    private final String typeOfCuisine;

    @NonNull
    private final String address;

    @NonNull
    private final String openingTime;

    @NonNull
    private final String distance;

    @NonNull
    private final String participants;

    @NonNull
    private final int stars;

    @NonNull
    private final String pictureUrl;

    public RestaurantsViewState(
        @NonNull String name,
        @NonNull String typeOfCuisine,
        @NonNull String address,
        @NonNull String openingTime,
        @NonNull String distance,
        @NonNull String participants,
        int stars,
        @NonNull String pictureUrl
    ) {
        this.name = name;
        this.typeOfCuisine = typeOfCuisine;
        this.address = address;
        this.openingTime = openingTime;
        this.distance = distance;
        this.participants = participants;
        this.stars = stars;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTypeOfCuisine() {
        return typeOfCuisine;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getOpeningTime() {
        return openingTime;
    }

    @NonNull
    public String getDistance() {
        return distance;
    }

    @NonNull
    public String getParticipants() {
        return participants;
    }

    @NonNull
    public int getStars() {
        return stars;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }
}
