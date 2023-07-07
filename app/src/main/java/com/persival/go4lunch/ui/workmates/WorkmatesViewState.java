package com.persival.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class WorkmatesViewState {

    // Workmate
    @NonNull
    private final String id;
    @NonNull
    private final String workmatePictureUrl;
    @NonNull
    private final String text;
    @Nullable
    private final String restaurantId;

    public WorkmatesViewState(
        @NonNull String id,
        @NonNull String workmatePictureUrl,
        @NonNull String text,
        @Nullable String restaurantId
    ) {
        this.id = id;
        this.workmatePictureUrl = workmatePictureUrl;
        this.text = text;
        this.restaurantId = restaurantId;
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
    public String getText() {
        return text;
    }

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkmatesViewState that = (WorkmatesViewState) o;
        return id.equals(that.id) &&
            workmatePictureUrl.equals(that.workmatePictureUrl) &&
            text.equals(that.text) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, text, restaurantId);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmatesViewState{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", text='" + text + '\'' +
            ", restaurantId='" + restaurantId + '\'' +
            '}';
    }
}
