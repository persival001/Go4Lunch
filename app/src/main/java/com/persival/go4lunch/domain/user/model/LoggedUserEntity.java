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
    private final String pictureUrl;
    @NonNull
    private final String emailAddress;

    public LoggedUserEntity(
        @NonNull String id,
        @NonNull String name,
        @Nullable String pictureUrl,
        @NonNull String emailAddress
    ) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.emailAddress = emailAddress;
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
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getEmailAddress() {
        return emailAddress;
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
            Objects.equals(pictureUrl, that.pictureUrl) &&
            emailAddress.equals(that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, emailAddress);
    }

    @NonNull
    @Override
    public String toString() {
        return "LoggedUserEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            '}';
    }
}