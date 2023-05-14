package com.persival.go4lunch.ui.main.settings;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SettingsViewState {

    @NonNull
    private final String id;
    @NonNull
    private final String avatarPictureUrl;
    @NonNull
    private final String avatarName;
    @NonNull
    private final String email;

    public SettingsViewState(
        @NonNull String id,
        @NonNull String avatarPictureUrl,
        @NonNull String avatarName,
        @NonNull String email
    ) {
        this.id = id;
        this.avatarPictureUrl = avatarPictureUrl;
        this.avatarName = avatarName;
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatarPictureUrl, avatarName, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SettingsViewState that = (SettingsViewState) o;
        return id.equals(that.id) && avatarPictureUrl.equals(that.avatarPictureUrl) && avatarName.equals(that.avatarName) && email.equals(that.email);
    }

    @NonNull
    @Override
    public String toString() {
        return "SettingsViewState{" +
            "id='" + id + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            ", avatarName='" + avatarName + '\'' +
            ", email='" + email + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @NonNull
    public String getAvatarName() {
        return avatarName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

}
