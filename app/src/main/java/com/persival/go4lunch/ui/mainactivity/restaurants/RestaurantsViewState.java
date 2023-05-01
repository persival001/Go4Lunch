package com.persival.go4lunch.ui.mainactivity.restaurants;

import androidx.annotation.NonNull;

import java.util.Objects;

public class RestaurantsViewState {
    private final long id;
    @NonNull
    private final String name;

    @NonNull
    private final String typeOfCuisineAndAddress;

    @NonNull
    private final String openingTime;

    @NonNull
    private final String distance;

    @NonNull
    private final String participants;

    private final float rating;

    @NonNull
    private final String pictureUrl;

    public RestaurantsViewState(long id,
                                @NonNull String name,
                                @NonNull String typeOfCuisineAndAddress,
                                @NonNull String openingTime,
                                @NonNull String distance,
                                @NonNull String participants,
                                float rating,
                                @NonNull String pictureUrl
    ) {
        this.id = id;
        this.name = name;
        this.typeOfCuisineAndAddress = typeOfCuisineAndAddress;
        this.openingTime = openingTime;
        this.distance = distance;
        this.participants = participants;
        this.rating = rating;
        this.pictureUrl = pictureUrl;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTypeOfCuisineAndAddress() {
        return typeOfCuisineAndAddress;
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
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public float getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            typeOfCuisineAndAddress,
            openingTime,
            distance,
            participants,
            pictureUrl,
            rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RestaurantsViewState that = (RestaurantsViewState) o;
        return name.equals(that.name)
            && id == that.id
            && typeOfCuisineAndAddress.equals(that.typeOfCuisineAndAddress)
            && openingTime.equals(that.openingTime)
            && distance.equals(that.distance)
            && participants.equals(that.participants)
            && pictureUrl.equals(that.pictureUrl)
            && rating == that.rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "RestaurantsViewState{" +
            "id=" + id +
            "name='" + name + '\'' +
            ", typeOfCuisineAndAddress='" + typeOfCuisineAndAddress + '\'' +
            ", openingTime='" + openingTime + '\'' +
            ", distance='" + distance + '\'' +
            ", participants='" + participants + '\'' +
            ", rating=" + rating +
            ", pictureUrl='" + pictureUrl + '\'' +
            '}';
    }
}
