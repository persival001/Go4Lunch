package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class LoggedUserDto {
    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    private final String emailAddress;
    @Nullable
    private final String avatarPictureUrl;
    @Nullable
    private final String idOfTheChosenRestaurant;

    // Empty constructor needed for Firestore deserialization
    public LoggedUserDto() {
        id = null;
        name = null;
        emailAddress = null;
        avatarPictureUrl = null;
        idOfTheChosenRestaurant = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, emailAddress, avatarPictureUrl, idOfTheChosenRestaurant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoggedUserDto user = (LoggedUserDto) o;
        return Objects.equals(id, user.id)
            && Objects.equals(name, user.name)
            && Objects.equals(emailAddress, user.emailAddress)
            && Objects.equals(avatarPictureUrl, user.avatarPictureUrl)
            && Objects.equals(idOfTheChosenRestaurant, user.idOfTheChosenRestaurant);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            ", idOfTheChosenRestaurant=" + idOfTheChosenRestaurant +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmailAddress() {
        return emailAddress;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Nullable
    public String getIdOfTheChosenRestaurant() {
        return idOfTheChosenRestaurant;
    }

}


