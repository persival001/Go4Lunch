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
    private final String emailAddress;
    @Nullable
    private final String avatarPictureUrl;

    // Empty constructor needed for Firestore deserialization
    public UserDto() {
        id = null;
        name = null;
        emailAddress = null;
        avatarPictureUrl = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, emailAddress, avatarPictureUrl);
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
            && Objects.equals(emailAddress, userDto.emailAddress)
            && Objects.equals(avatarPictureUrl, userDto.avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
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
}


