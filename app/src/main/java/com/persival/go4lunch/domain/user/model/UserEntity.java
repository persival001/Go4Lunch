package com.persival.go4lunch.domain.user.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserEntity {
    @NonNull
    private final LoggedUserEntity loggedUserEntity;
    @Nullable
    private final String goingToPlaceId;

    public UserEntity(@NonNull LoggedUserEntity loggedUserEntity, @Nullable String goingToPlaceId) {
        this.loggedUserEntity = loggedUserEntity;
        this.goingToPlaceId = goingToPlaceId;
    }
}
