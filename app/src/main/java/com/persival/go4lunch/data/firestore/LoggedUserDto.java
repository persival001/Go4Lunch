package com.persival.go4lunch.data.firestore;

import androidx.annotation.Nullable;

import java.util.Objects;

public class LoggedUserDto {

    // Logged user
    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    private final String emailAddress;
    @Nullable
    private final String avatarPictureUrl;

    // Associated restaurant
    @Nullable
    private final String restaurantId;
    @Nullable
    private final String restaurantName;
    @Nullable
    private final String restaurantAddress;

    // Empty constructor needed for Firestore deserialization
    public LoggedUserDto() {
        id = null;
        name = null;
        emailAddress = null;
        avatarPictureUrl = null;
        restaurantId = null;
        restaurantName = null;
        restaurantAddress = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, emailAddress, avatarPictureUrl, restaurantId, restaurantName, restaurantAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoggedUserDto that = (LoggedUserDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(avatarPictureUrl, that.avatarPictureUrl) && Objects.equals(restaurantId, that.restaurantId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(restaurantAddress, that.restaurantAddress);
    }

    @Override
    public String toString() {
        return "LoggedUserDto{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
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
    public String getName() {
        return name;
    }

    @Nullable
    public String getEmailAddress() {
        return emailAddress;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
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


