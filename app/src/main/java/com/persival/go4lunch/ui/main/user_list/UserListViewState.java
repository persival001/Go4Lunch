package com.persival.go4lunch.ui.main.user_list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class UserListViewState {

    @NonNull
    private final String uId;

    @NonNull
    private final String name;

    @NonNull
    private final String emailAddress;

    @NonNull
    private final String avatarPictureUrl;

    public UserListViewState(
        @NonNull final String uId,
        @NonNull final String name,
        @NonNull final String emailAddress,
        @NonNull final String avatarPictureUrl
    ) {
        this.uId = uId;
        this.name = name;
        this.emailAddress = emailAddress;
        this.avatarPictureUrl = avatarPictureUrl;
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

    @NonNull
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getuId(), getName(), getEmailAddress(), getAvatarPictureUrl());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserListViewState))
            return false;
        final UserListViewState that = (UserListViewState) o;
        return Objects.equals(getuId(), that.getuId()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getEmailAddress(), that.getEmailAddress()) &&
            Objects.equals(getAvatarPictureUrl(), that.getAvatarPictureUrl());
    }

    @Override
    public String toString() {
        return "UserListViewState{" +
            "uId='" + uId + '\'' +
            ", name='" + name + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            '}';
    }

}
