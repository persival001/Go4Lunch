package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class UserRestaurantRelationsDto {
    @Nullable
    private final String userId;
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final Date dateOfVisit;


    // Empty constructor needed for Firestore deserialization
    public UserRestaurantRelationsDto() {
        userId = null;
        restaurantId = null;
        restaurantName = null;
        dateOfVisit = null;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
    }

    @Nullable
    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserRestaurantRelationsDto that = (UserRestaurantRelationsDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(restaurantId, that.restaurantId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(dateOfVisit, that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, restaurantId, restaurantName, dateOfVisit);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserRestaurantRelationsDto{" +
            "userId='" + userId + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
