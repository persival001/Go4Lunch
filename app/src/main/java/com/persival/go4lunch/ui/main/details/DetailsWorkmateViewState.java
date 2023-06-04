package com.persival.go4lunch.ui.main.details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DetailsWorkmateViewState {

    @NonNull
    private final String id;
    @NonNull
    private final String workmatePictureUrl;
    @NonNull
    private final String workmateName;

    public DetailsWorkmateViewState(
        @NonNull String id,
        @NonNull String workmatePictureUrl,
        @NonNull String workmateName
    ) {
        this.id = id;
        this.workmatePictureUrl = workmatePictureUrl;
        this.workmateName = workmateName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, workmateName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DetailsWorkmateViewState that = (DetailsWorkmateViewState) o;
        return id.equals(that.id) && workmatePictureUrl.equals(that.workmatePictureUrl) && workmateName.equals(that.workmateName);
    }

    @Override
    public String toString() {
        return "DetailsWorkmateViewState{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", workmateName='" + workmateName + '\'' +
            '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getWorkmatePictureUrl() {
        return workmatePictureUrl;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }
}