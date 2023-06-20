package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class UserEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @Nullable
    private final String pictureUrl;
    @Nullable
    private final List<String> likedRestaurantsId;

    public UserEntity(
        @NonNull String id,
        @NonNull String name,
        @Nullable String pictureUrl,
        @Nullable List<String> likedRestaurantsId
    ) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.likedRestaurantsId = likedRestaurantsId;
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

    @Nullable
    public List<String> getLikedRestaurantsId() {
        return likedRestaurantsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(pictureUrl, that.pictureUrl) && Objects.equals(likedRestaurantsId, that.likedRestaurantsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pictureUrl, likedRestaurantsId);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", likedRestaurantsId=" + likedRestaurantsId +
            '}';
    }
}