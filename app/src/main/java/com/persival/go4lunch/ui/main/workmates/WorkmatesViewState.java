package com.persival.go4lunch.ui.main.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewState {

    // Workmate
    @NonNull
    private final String id;
    @NonNull
    private final String workmatePictureUrl;
    @NonNull
    private final String workmateName;

    // Associated restaurant
    @NonNull
    private final String restaurantName;

    public WorkmatesViewState(
        @NonNull String id,
        @NonNull String workmatePictureUrl,
        @NonNull String workmateName,
        @NonNull String restaurantName
    ) {
        this.id = id;
        this.workmatePictureUrl = workmatePictureUrl;
        this.workmateName = workmateName;
        this.restaurantName = restaurantName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, workmateName, restaurantName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkmatesViewState that = (WorkmatesViewState) o;
        return id.equals(that.id) && workmatePictureUrl.equals(that.workmatePictureUrl) && workmateName.equals(that.workmateName) && restaurantName.equals(that.restaurantName);
    }

    @Override
    public String toString() {
        return "WorkmatesViewState{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getWorkmatePictureUrl() {
        return workmatePictureUrl;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }
}
