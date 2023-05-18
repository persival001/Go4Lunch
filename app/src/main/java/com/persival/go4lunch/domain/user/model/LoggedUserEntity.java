package com.persival.go4lunch.domain.user.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class LoggedUserEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @Nullable
    private final String avatarPictureUrl;

    public LoggedUserEntity(
        @NonNull String id,
        @NonNull String name,
        @Nullable String avatarPictureUrl
    ) {
        this.id = id;
        this.name = name;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggedUserEntity that = (LoggedUserEntity) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(avatarPictureUrl, that.avatarPictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            '}';
    }
}
