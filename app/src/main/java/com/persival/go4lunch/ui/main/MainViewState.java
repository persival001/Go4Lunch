package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MainViewState {

    @NonNull
    private final String id;
    @NonNull
    private final String avatarName;
    @NonNull
    private final String email;
    @Nullable
    private final String avatarPictureUrl;

    public MainViewState(
        @NonNull String id,
        @NonNull String avatarName,
        @NonNull String email,
        @Nullable String avatarPictureUrl
    ) {
        this.id = id;
        this.avatarName = avatarName;
        this.email = email;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getAvatarName() {
        return avatarName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MainViewState that = (MainViewState) o;
        return id.equals(that.id) && avatarName.equals(that.avatarName) && email.equals(that.email) && Objects.equals(avatarPictureUrl, that.avatarPictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatarName, email, avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "MainViewState{" +
            "id='" + id + '\'' +
            ", avatarName='" + avatarName + '\'' +
            ", email='" + email + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            '}';
    }
}
