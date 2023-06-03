package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class WorkmateEntity {

    // Workmate
    @NonNull
    private final String id;
    @Nullable
    private final String workmatePictureUrl;
    @NonNull
    private final String workmateName;

    // Associated restaurant
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final String restaurantAddress;

    public WorkmateEntity(
        @NonNull String id,
        @Nullable String workmatePictureUrl,
        @NonNull String workmateName,
        @Nullable String restaurantId,
        @Nullable String restaurantName,
        @Nullable String restaurantAddress
    ) {
        this.id = id;
        this.workmatePictureUrl = workmatePictureUrl;
        this.workmateName = workmateName;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, workmateName, restaurantId, restaurantName, restaurantAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkmateEntity that = (WorkmateEntity) o;
        return id.equals(that.id) &&
            Objects.equals(workmatePictureUrl, that.workmatePictureUrl) &&
            workmateName.equals(that.workmateName) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(restaurantName, that.restaurantName) &&
            Objects.equals(restaurantAddress, that.restaurantAddress);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateEntity{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", restaurantAddress='" + restaurantAddress + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getWorkmatePictureUrl() {
        return workmatePictureUrl;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
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
    public String getRestaurantAddress() {
        return restaurantAddress;
    }
}