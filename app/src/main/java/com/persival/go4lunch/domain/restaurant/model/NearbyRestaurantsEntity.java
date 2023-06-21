package com.persival.go4lunch.domain.restaurant.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class NearbyRestaurantsEntity {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String address;


    private final boolean openingHours;

    private final float rating;

    @Nullable
    private final List<String> photos;

    private final double latitude;

    private final double longitude;

    public NearbyRestaurantsEntity(
        @NonNull String id,
        @NonNull String name,
        @NonNull String address,
        boolean openingHours,
        float rating,
        @Nullable List<String> photos,
        double latitude,
        double longitude
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
        this.rating = rating;
        this.photos = photos;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public boolean isOpeningHours() {
        return openingHours;
    }

    public float getRating() {
        return rating;
    }

    @Nullable
    public List<String> getPhotos() {
        return photos;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NearbyRestaurantsEntity that = (NearbyRestaurantsEntity) o;
        return openingHours == that.openingHours && Float.compare(that.rating, rating) == 0 && Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && id.equals(that.id) && name.equals(that.name) && address.equals(that.address) && Objects.equals(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openingHours, rating, photos, latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
