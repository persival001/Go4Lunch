package com.persival.go4lunch.data.firestore;

import androidx.annotation.Nullable;

import java.util.Objects;

public class WorkmateDto {

    // Workmate
    @Nullable
    private final String id;
    @Nullable
    private final String workmatePictureUrl;
    @Nullable
    private final String workmateName;

    // Associated restaurant
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final String restaurantAddress;


    // Empty constructor needed for Firestore deserialization
    public WorkmateDto() {
        id = null;
        workmatePictureUrl = null;
        workmateName = null;
        restaurantId = null;
        restaurantName = null;
        restaurantAddress = null;
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
        WorkmateDto that = (WorkmateDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(workmatePictureUrl, that.workmatePictureUrl) &&
            Objects.equals(workmateName, that.workmateName) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(restaurantName, that.restaurantName) &&
            Objects.equals(restaurantAddress, that.restaurantAddress);
    }

    @Override
    public String toString() {
        return "WorkmateDto{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            ", restaurantAddress='" + restaurantAddress + '\'' +
            '}';
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getWorkmatePictureUrl() {
        return workmatePictureUrl;
    }

    @Nullable
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


