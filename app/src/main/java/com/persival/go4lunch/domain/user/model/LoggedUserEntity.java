package com.persival.go4lunch.domain.user.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class LoggedUserEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String emailAddress;
    @Nullable
    private final String avatarPictureUrl;

    public LoggedUserEntity(
        @NonNull String id,
        @NonNull String name,
        @NonNull String emailAddress,
        @Nullable String avatarPictureUrl
    ) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.avatarPictureUrl = avatarPictureUrl;
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
        LoggedUserEntity that = (LoggedUserEntity) o;
        return id.equals(that.id) &&
            name.equals(that.name) &&
            emailAddress.equals(that.emailAddress) &&
            Objects.equals(avatarPictureUrl, that.avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "LoggedUserEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
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
}