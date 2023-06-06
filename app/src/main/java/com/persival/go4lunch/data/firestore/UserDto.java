package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class UserDto {

    // Workmate
    @Nullable
    private final String id;
    @Nullable
    private final String workmatePictureUrl;
    @Nullable
    private final String workmateName;
    @Nullable
    private final List<String> likedRestaurantsId;


    // Empty constructor needed for Firestore deserialization
    public UserDto() {
        id = null;
        workmatePictureUrl = null;
        workmateName = null;
        likedRestaurantsId = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, workmateName, likedRestaurantsId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDto that = (UserDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(workmatePictureUrl, that.workmatePictureUrl) &&
            Objects.equals(workmateName, that.workmateName) &&
            Objects.equals(likedRestaurantsId, that.likedRestaurantsId);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateDto{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", restaurantId='" + likedRestaurantsId + '\'' +
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
    public List<String> getLikedRestaurantsId() {
        return likedRestaurantsId;
    }

}


