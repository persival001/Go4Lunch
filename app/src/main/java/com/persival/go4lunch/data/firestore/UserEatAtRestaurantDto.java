package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class UserEatAtRestaurantDto {
    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    private final String pictureUrl;
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final Date dateOfVisit;

    // Empty constructor needed for Firestore deserialization
    public UserEatAtRestaurantDto() {
        id = null;
        name = null;
        pictureUrl = null;
        restaurantId = null;
        restaurantName = null;
        dateOfVisit = null;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
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
        UserEatAtRestaurantDto that = (UserEatAtRestaurantDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(pictureUrl, that.pictureUrl) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(restaurantName, that.restaurantName) &&
            Objects.equals(dateOfVisit, that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, restaurantId, restaurantName, dateOfVisit);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserEatAtRestaurantDto{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
