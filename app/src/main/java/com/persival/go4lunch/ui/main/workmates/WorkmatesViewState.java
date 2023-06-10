package com.persival.go4lunch.ui.main.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewState {

    // Workmate
    @NonNull
    private final String id;
    @NonNull
    private final String workmatePictureUrl;
    @NonNull
    private final String text;

    public WorkmatesViewState(
        @NonNull String id,
        @NonNull String workmatePictureUrl,
        @NonNull String text
    ) {
        this.id = id;
        this.workmatePictureUrl = workmatePictureUrl;
        this.text = text;
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
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WorkmatesViewState that = (WorkmatesViewState) o;
        return id.equals(that.id) && workmatePictureUrl.equals(that.workmatePictureUrl) && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmatePictureUrl, text);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmatesViewState{" +
            "id='" + id + '\'' +
            ", workmatePictureUrl='" + workmatePictureUrl + '\'' +
            ", text='" + text + '\'' +
            '}';
    }
}
