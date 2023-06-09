package com.persival.go4lunch.domain.restaurant.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class UserEatAtRestaurantEntity {
    @NonNull
    private final String userId;
    @NonNull
    private final String restaurantId;
    @NonNull
    private final Date dateOfVisit;


    public UserEatAtRestaurantEntity(
        @NonNull String userId,
        @NonNull String restaurantId,
        @NonNull Date dateOfVisit
    ) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateOfVisit = dateOfVisit;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserEatAtRestaurantEntity{" +
            "userId='" + userId + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEatAtRestaurantEntity that = (UserEatAtRestaurantEntity) o;
        return userId.equals(that.userId) && restaurantId.equals(that.restaurantId) && dateOfVisit.equals(that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, restaurantId, dateOfVisit);
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
    public Date getDateOfVisit() {
        return dateOfVisit;
    }
}
