package com.persival.go4lunch.domain.details.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotificationEntity {

    @NonNull
    private final String restaurantName;
    @NonNull
    private final String restaurantAddress;
    @NonNull
    private final List<String> workmatesNames;
    @NonNull
    private final Date dateOfVisit;

    public NotificationEntity(
        @NonNull String restaurantName,
        @NonNull String restaurantAddress,
        @NonNull List<String> workmatesNames,
        @NonNull Date dateOfVisit
    ) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.workmatesNames = workmatesNames;
        this.dateOfVisit = dateOfVisit;
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
    public List<String> getWorkmatesNames() {
        return workmatesNames;
    }

    @NonNull
    public Date getDateOfVisit() {
        return dateOfVisit;
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
            workmatesNames.equals(that.workmatesNames) &&
            dateOfVisit.equals(that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName, restaurantAddress, workmatesNames, dateOfVisit);
    }

    @Override
    public String toString() {
        return "NotificationEntity{" +
            "restaurantName='" + restaurantName + '\'' +
            ", restaurantAddress='" + restaurantAddress + '\'' +
            ", workmatesNames=" + workmatesNames +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
