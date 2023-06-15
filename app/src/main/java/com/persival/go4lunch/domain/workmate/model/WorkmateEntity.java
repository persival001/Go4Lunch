package com.persival.go4lunch.domain.workmate.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class WorkmateEntity {

    @NonNull
    private final String id;
    @Nullable
    private final String pictureUrl;
    @NonNull
    private final String name;
    @Nullable
    private final List<String> likedRestaurantsId;

    public WorkmateEntity(
        @NonNull String id,
        @Nullable String pictureUrl,
        @NonNull String name,
        @Nullable List<String> likedRestaurantsId
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.likedRestaurantsId = likedRestaurantsId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getName() {
        return name;
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
        WorkmateEntity that = (WorkmateEntity) o;
        return id.equals(that.id) &&
            Objects.equals(pictureUrl, that.pictureUrl) &&
            name.equals(that.name) &&
            Objects.equals(likedRestaurantsId, that.likedRestaurantsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, likedRestaurantsId);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateEntity{" +
            "id='" + id + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", likedRestaurantsId=" + likedRestaurantsId +
            '}';
    }
}