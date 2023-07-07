package com.persival.go4lunch.ui.restaurants;

import androidx.annotation.NonNull;

import java.util.Objects;

public class RestaurantsViewState {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String address;
    @NonNull
    private final Boolean openingTime;
    @NonNull
    private final String distance;
    @NonNull
    private final String participants;
    private final float rating;
    @NonNull
    private final String pictureUrl;

    public RestaurantsViewState(
        @NonNull String id,
        @NonNull String name,
        @NonNull String address,
        @NonNull Boolean openingTime,
        @NonNull String distance,
        @NonNull String participants,
        float rating,
        @NonNull String pictureUrl
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.distance = distance;
        this.participants = participants;
        this.rating = rating;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTypeOfCuisineAndAddress() {
        return address;
    }

    @NonNull
    public Boolean getOpeningTime() {
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

    public float getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            address,
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
            && id.equals(that.id)
            && address.equals(that.address)
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
            ", typeOfCuisineAndAddress='" + address + '\'' +
            ", openingTime='" + openingTime + '\'' +
            ", distance='" + distance + '\'' +
            ", participants='" + participants + '\'' +
            ", rating=" + rating +
            ", pictureUrl='" + pictureUrl + '\'' +
            '}';
    }
}
