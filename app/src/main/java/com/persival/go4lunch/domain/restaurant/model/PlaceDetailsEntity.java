package com.persival.go4lunch.domain.restaurant.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.persival.go4lunch.data.places.model.PlaceDetailsResponse;

import java.util.List;
import java.util.Objects;

public class PlaceDetailsEntity {

    @NonNull
    private final String id;

    @Nullable
    private final List<PlaceDetailsResponse.PhotoDetails> photoUrl;

    @NonNull
    private final String name;

    private final float rating;

    @NonNull
    private final String address;

    @Nullable
    private final String phoneNumber;

    @Nullable
    private final String website;

    public PlaceDetailsEntity(
        @NonNull String id,
        @Nullable List<PlaceDetailsResponse.PhotoDetails> photoUrl,
        @NonNull String name,
        float rating,
        @NonNull String address,
        @Nullable String phoneNumber,
        @Nullable String website
    ) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public List<PlaceDetailsResponse.PhotoDetails> getPhotoUrl() {
        return photoUrl;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlaceDetailsEntity that = (PlaceDetailsEntity) o;
        return Float.compare(that.rating, rating) == 0 &&
            id.equals(that.id) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            name.equals(that.name) && address.equals(that.address) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, photoUrl, name, rating, address, phoneNumber, website);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceDetailsEntity{" +
            "id='" + id + '\'' +
            ", photoUrl=" + photoUrl +
            ", name='" + name + '\'' +
            ", rating=" + rating +
            ", address='" + address + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            '}';
    }
}
