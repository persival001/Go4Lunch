package com.persival.go4lunch.domain.user.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class UserEntity {
    @NonNull
    private final LoggedUserEntity loggedUserEntity;

    @Nullable
    private final String goingToPlaceId;

    public UserEntity(
        @NonNull LoggedUserEntity loggedUserEntity,
        @Nullable String goingToPlaceId
    ) {
        this.loggedUserEntity = loggedUserEntity;
        this.goingToPlaceId = goingToPlaceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loggedUserEntity, goingToPlaceId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return loggedUserEntity.equals(that.loggedUserEntity) &&
            Objects.equals(goingToPlaceId, that.goingToPlaceId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
            "loggedUserEntity=" + loggedUserEntity +
            ", goingToPlaceId='" + goingToPlaceId + '\'' +
            '}';
    }

    @NonNull
    public LoggedUserEntity getLoggedUserEntity() {
        return loggedUserEntity;
    }

    @Nullable
    public String getGoingToPlaceId() {
        return goingToPlaceId;
    }
}
