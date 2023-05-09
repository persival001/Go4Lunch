package com.persival.go4lunch.ui.main.user_list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class UserListViewState {

    @NonNull
    private final String id;
    @NonNull
    private final String avatarPictureUrl;
    @NonNull
    private final String avatarName;
    @NonNull
    private final String restaurantName;
    public UserListViewState(
        @NonNull String id,
        @NonNull String avatarPictureUrl,
        @NonNull String avatarName,
        @NonNull String restaurantName
    ) {
        this.id = id;
        this.avatarPictureUrl = avatarPictureUrl;
        this.avatarName = avatarName;
        this.restaurantName = restaurantName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatarPictureUrl, avatarName, restaurantName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserListViewState that = (UserListViewState) o;
        return id.equals(that.id) && avatarPictureUrl.equals(that.avatarPictureUrl) && avatarName.equals(that.avatarName) && restaurantName.equals(that.restaurantName);
    }

    @Override
    public String toString() {
        return "UserListViewState{" +
            "id='" + id + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            ", avatarName='" + avatarName + '\'' +
            ", restaurantName='" + restaurantName + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @NonNull
    public String getAvatarName() {
        return avatarName;
    }

    @NonNull
    public String getRestaurantName() {
        return restaurantName;
    }


}
