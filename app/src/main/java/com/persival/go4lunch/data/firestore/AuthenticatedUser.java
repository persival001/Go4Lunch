package com.persival.go4lunch.data.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class AuthenticatedUser {
    @NonNull
    private String uId;
    @NonNull
    private String name;
    @NonNull
    private String emailAddress;
    @Nullable
    private String avatarPictureUrl;

    // Empty constructor needed for Firebase deserialization
    public AuthenticatedUser() {
    }

    public AuthenticatedUser(
        @NonNull String uId,
        @NonNull String name,
        @NonNull String emailAddress,
        @Nullable String avatarPictureUrl
    ) {
        this.uId = uId;
        this.name = name;
        this.emailAddress = emailAddress;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId, name, emailAddress, avatarPictureUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthenticatedUser user = (AuthenticatedUser) o;
        return Objects.equals(uId, user.uId)
            && Objects.equals(name, user.name)
            && Objects.equals(emailAddress, user.emailAddress)
            && Objects.equals(avatarPictureUrl, user.avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "uId='" + uId + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
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

    @NonNull
    public String getEmailAddress() {
        return emailAddress;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

}


