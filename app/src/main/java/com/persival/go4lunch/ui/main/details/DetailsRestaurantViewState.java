package com.persival.go4lunch.ui.main.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class DetailsRestaurantViewState {
    @NonNull
    private final String id;
    @Nullable
    private final String restaurantPictureUrl;
    @NonNull
    private final String restaurantName;
    private final Float restaurantRating;
    @NonNull
    private final String restaurantAddress;
    @Nullable
    private final String restaurantPhoneNumber;
    @Nullable
    private final String restaurantWebsiteUrl;

    public DetailsRestaurantViewState(
        @NonNull String id,
        @Nullable String restaurantPictureUrl,
        @NonNull String restaurantName,
        Float restaurantRating,
        @NonNull String restaurantAddress,
        @Nullable String restaurantPhoneNumber,
        @Nullable String restaurantWebsiteUrl
    ) {
        this.id = id;
        this.restaurantPictureUrl = restaurantPictureUrl;
        this.restaurantName = restaurantName;
        this.restaurantRating = restaurantRating;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhoneNumber = restaurantPhoneNumber;
        this.restaurantWebsiteUrl = restaurantWebsiteUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DetailsRestaurantViewState that = (DetailsRestaurantViewState) o;
        return id.equals(that.id) &&
            Objects.equals(restaurantPictureUrl, that.restaurantPictureUrl) &&
            restaurantName.equals(that.restaurantName) &&
            Objects.equals(restaurantRating, that.restaurantRating) &&
            restaurantAddress.equals(that.restaurantAddress) &&
            Objects.equals(restaurantPhoneNumber, that.restaurantPhoneNumber) &&
            Objects.equals(restaurantWebsiteUrl, that.restaurantWebsiteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantPictureUrl, restaurantName, restaurantRating, restaurantAddress, restaurantPhoneNumber, restaurantWebsiteUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailsRestaurantViewState{" +
            "id='" + id + '\'' +
            ", restaurantPictureUrl='" + restaurantPictureUrl + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", restaurantRating=" + restaurantRating +
            ", restaurantAddress='" + restaurantAddress + '\'' +
            ", restaurantPhoneNumber='" + restaurantPhoneNumber + '\'' +
            ", restaurantWebsiteUrl='" + restaurantWebsiteUrl + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getRestaurantPictureUrl() {
        return restaurantPictureUrl;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    public Float getRestaurantRating() {
        return restaurantRating;
    }

    @NonNull
    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    @Nullable
    public String getRestaurantPhoneNumber() {
        return restaurantPhoneNumber;
    }

    @Nullable
    public String getRestaurantWebsiteUrl() {
        return restaurantWebsiteUrl;
    }
}