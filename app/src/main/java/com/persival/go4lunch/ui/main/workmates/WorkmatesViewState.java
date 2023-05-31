package com.persival.go4lunch.ui.main.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

// TODO Persival un bon coup de katana s'impose!
public class WorkmatesViewState {

    @NonNull
    private final String uId;

    @NonNull
    private final String name;

    @NonNull
    private final String emailAddress;

    @Nullable
    private final String avatarPictureUrl;

    public WorkmatesViewState(
        @NonNull final String uId,
        @NonNull final String name,
        @NonNull final String emailAddress,
        @Nullable final String avatarPictureUrl
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

    @Nullable
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
        if (!(o instanceof WorkmatesViewState))
            return false;
        final WorkmatesViewState that = (WorkmatesViewState) o;
        return Objects.equals(getuId(), that.getuId()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getEmailAddress(), that.getEmailAddress()) &&
            Objects.equals(getAvatarPictureUrl(), that.getAvatarPictureUrl());
    }

    @NonNull
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
