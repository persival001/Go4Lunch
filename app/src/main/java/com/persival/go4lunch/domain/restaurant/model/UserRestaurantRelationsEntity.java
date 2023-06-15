package com.persival.go4lunch.domain.restaurant.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class UserRestaurantRelationsEntity {
    @NonNull
    private final String userId;
    @NonNull
    private final String restaurantId;
    @NonNull
    private final String restaurantName;
    @NonNull
    private final Date dateOfVisit;

    public UserRestaurantRelationsEntity(
        @NonNull String userId,
        @NonNull String restaurantId,
        @NonNull String restaurantName,
        @NonNull Date dateOfVisit
    ) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.dateOfVisit = dateOfVisit;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getRestaurantId() {
        return restaurantId;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
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
        UserRestaurantRelationsEntity that = (UserRestaurantRelationsEntity) o;
        return userId.equals(that.userId) &&
            restaurantId.equals(that.restaurantId) &&
            restaurantName.equals(that.restaurantName) &&
            dateOfVisit.equals(that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, restaurantId, restaurantName, dateOfVisit);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserRestaurantRelationsEntity{" +
            "userId='" + userId + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
