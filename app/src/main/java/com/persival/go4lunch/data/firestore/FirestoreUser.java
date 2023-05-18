package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class FirestoreUser {
    @NonNull
    private String uId;
    @NonNull
    private String name;
    @Nullable
    private String avatarPictureUrl;

    private boolean isAtRestaurant;

    private boolean isFavoriteRestaurant;

    // Empty constructor needed for Firestore deserialization
    public FirestoreUser() {
    }

    public FirestoreUser(
        @NonNull String uId,
        @NonNull String name,
        @Nullable String avatarPictureUrl,
        boolean isAtRestaurant,
        boolean isFavoriteRestaurant
    ) {
        this.uId = uId;
        this.name = name;
        this.avatarPictureUrl = avatarPictureUrl;
        this.isAtRestaurant = isAtRestaurant;
        this.isFavoriteRestaurant = isFavoriteRestaurant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId, name, avatarPictureUrl, isAtRestaurant, isFavoriteRestaurant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FirestoreUser firestoreUser = (FirestoreUser) o;
        return Objects.equals(uId, firestoreUser.uId)
            && Objects.equals(name, firestoreUser.name)
            && Objects.equals(avatarPictureUrl, firestoreUser.avatarPictureUrl)
            && Objects.equals(isAtRestaurant, firestoreUser.isAtRestaurant)
            && Objects.equals(isFavoriteRestaurant, firestoreUser.isFavoriteRestaurant);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "uId='" + uId + '\'' +
            ", name='" + name + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            ", isAtRestaurant=" + isAtRestaurant +
            ", isFavoriteRestaurant=" + isFavoriteRestaurant +
            '}';
    }

    @NonNull
    public String getuId() {
        return uId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    public Boolean getIsAtRestaurant() {
        return isAtRestaurant;
    }

    public Boolean getIsFavoriteRestaurant() {
        return isFavoriteRestaurant;
    }

}


