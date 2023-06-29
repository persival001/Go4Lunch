package com.persival.go4lunch.domain.details.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class NotificationEntity {

    @NonNull
    private final String restaurantName;
    @NonNull
    private final String restaurantAddress;
    @NonNull
    private final String workmatesNames;

    public NotificationEntity(
        @NonNull String restaurantName,
        @NonNull String restaurantAddress,
        @NonNull String workmatesNames
    ) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.workmatesNames = workmatesNames;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @NonNull
    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    @NonNull
    public String getWorkmatesNames() {
        return workmatesNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NotificationEntity that = (NotificationEntity) o;
        return restaurantName.equals(that.restaurantName) &&
            restaurantAddress.equals(that.restaurantAddress) &&
            workmatesNames.equals(that.workmatesNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName, restaurantAddress, workmatesNames);
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationEntity{" +
            "restaurantName='" + restaurantName + '\'' +
            ", restaurantAddress='" + restaurantAddress + '\'' +
            ", workmatesNames='" + workmatesNames + '\'' +
            '}';
    }
}
