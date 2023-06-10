package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class WorkmateEatAtRestaurantEntity {

    @NonNull
    private final String id;
    @Nullable
    private final String pictureUrl;
    @NonNull
    private final String name;
    @NonNull
    private final String restaurantName;

    public WorkmateEatAtRestaurantEntity(
        @NonNull String id,
        @Nullable String pictureUrl,
        @NonNull String name,
        @NonNull String restaurantName
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.restaurantName = restaurantName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkmateEatAtRestaurantEntity that = (WorkmateEatAtRestaurantEntity) o;
        return id.equals(that.id) && Objects.equals(pictureUrl, that.pictureUrl) && name.equals(that.name) && restaurantName.equals(that.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, restaurantName);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateEatAtRestaurant{" +
            "id='" + id + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            '}';
    }
}
