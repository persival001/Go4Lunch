package com.persival.go4lunch.ui.main;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MainViewStateAutocomplete {

    private final String placeId;
    private final String name;

    public MainViewStateAutocomplete(String placeId, String name) {
        this.placeId = placeId;
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainViewStateAutocomplete that = (MainViewStateAutocomplete) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, name);
    }

    @NonNull
    @Override
    public String toString() {
        return "MainViewStateAutocomplete{" +
            "placeId='" + placeId + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
