package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class UserEatAtRestaurantEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @Nullable
    private final String pictureUrl;
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final Date dateOfVisit;

    public UserEatAtRestaurantEntity(
        @NonNull String id,
        @NonNull String name,
        @Nullable String pictureUrl,
        @Nullable String restaurantId,
        @Nullable String restaurantName,
        @Nullable Date dateOfVisit
    ) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.dateOfVisit = dateOfVisit;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
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
        UserEatAtRestaurantEntity that = (UserEatAtRestaurantEntity) o;
        return id.equals(that.id) &&
            name.equals(that.name) &&
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
        return "UserEatAtRestaurantEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
