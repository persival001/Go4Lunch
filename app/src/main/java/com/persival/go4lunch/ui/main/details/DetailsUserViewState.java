package com.persival.go4lunch.ui.main.details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DetailsUserViewState {

    @NonNull
    private final String uid;
    @NonNull
    private final String name;
    @NonNull
    private final String avatarPictureUrl;

    public DetailsUserViewState(@NonNull String uid,
                                @NonNull String name,
                                @NonNull String avatarPictureUrl
    ) {
        this.uid = uid;
        this.name = name;
        this.avatarPictureUrl = avatarPictureUrl;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAvatarPictureUrl() {
        return avatarPictureUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, avatarPictureUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DetailsUserViewState that = (DetailsUserViewState) o;

        if (!uid.equals(that.uid))
            return false;
        if (!name.equals(that.name))
            return false;
        return avatarPictureUrl.equals(that.avatarPictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailsUserViewState{" +
            "uid='" + uid + '\'' +
            ", name='" + name + '\'' +
            ", avatarPictureUrl='" + avatarPictureUrl + '\'' +
            '}';
    }

}
