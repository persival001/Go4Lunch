package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class UserDto {
    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    private final String avatarPictureUrl;
    @Nullable
    private final Boolean isAtRestaurant;

    // Empty constructor needed for Firestore deserialization
    public UserDto() {
        id = null;
        name = null;
        avatarPictureUrl = null;
        isAtRestaurant = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarPictureUrl, isAtRestaurant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id)
            && Objects.equals(name, userDto.name)
            && Objects.equals(avatarPictureUrl, userDto.avatarPictureUrl)
            && Objects.equals(isAtRestaurant, userDto.isAtRestaurant);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            ", isAtRestaurant=" + isAtRestaurant +
            '}';
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
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Nullable
    public Boolean getAtRestaurant() {
        return isAtRestaurant;
    }
}


