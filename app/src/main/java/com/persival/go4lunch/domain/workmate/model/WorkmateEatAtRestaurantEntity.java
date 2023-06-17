package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class WorkmateEatAtRestaurantEntity {

    @NonNull
    private final String id;
    @Nullable
    private final String pictureUrl;
    @NonNull
    private final String name;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final String restaurantId;
    @Nullable
    private final Date dateOfVisit;

    public WorkmateEatAtRestaurantEntity(
        @NonNull String id,
        @Nullable String pictureUrl,
        @NonNull String name,
        @Nullable String restaurantName,
        @Nullable String restaurantId,
        @Nullable Date dateOfVisit
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
        this.dateOfVisit = dateOfVisit;
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

    @Nullable
    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
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
        WorkmateEatAtRestaurantEntity that = (WorkmateEatAtRestaurantEntity) o;
        return id.equals(that.id) &&
            Objects.equals(pictureUrl, that.pictureUrl) &&
            name.equals(that.name) &&
            Objects.equals(restaurantName, that.restaurantName) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(dateOfVisit, that.dateOfVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, restaurantName, restaurantId, dateOfVisit);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateEatAtRestaurantEntity{" +
            "id='" + id + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", dateOfVisit=" + dateOfVisit +
            '}';
    }
}
