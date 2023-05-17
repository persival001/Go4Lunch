package com.persival.go4lunch.ui.main.details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DetailsViewState {
    @NonNull
    private final String id;
    @NonNull
    private final String pictureUrl;
    @NonNull
    private final String name;
    private final Float rating;
    @NonNull
    private final String address;
    @NonNull
    private final String phoneNumber;
    @NonNull
    private final String website;
    @NonNull
    private final Boolean isLiked;
    @NonNull
    private final Boolean isChoosen;

    public DetailsViewState(
        @NonNull String id,
        @NonNull String pictureUrl,
        @NonNull String name, Float rating,
        @NonNull String address,
        @NonNull String phoneNumber,
        @NonNull String website,
        @NonNull Boolean isLiked,
        @NonNull Boolean isChoosen
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.isLiked = isLiked;
        this.isChoosen = isChoosen;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Float getRating() {
        return rating;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    @NonNull
    public Boolean getLiked() {
        return isLiked;
    }

    @NonNull
    public Boolean getChoosen() {
        return isChoosen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, rating, address, phoneNumber, website, isLiked, isChoosen);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DetailsViewState that = (DetailsViewState) o;
        return id.equals(that.id) &&
            pictureUrl.equals(that.pictureUrl) &&
            name.equals(that.name) &&
            Objects.equals(rating, that.rating) &&
            address.equals(that.address) &&
            phoneNumber.equals(that.phoneNumber) &&
            website.equals(that.website) &&
            isLiked.equals(that.isLiked) &&
            isChoosen.equals(that.isChoosen);
    }

    @Override
    public String toString() {
        return "DetailsViewState{" +
            "id='" + id + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", rating=" + rating +
            ", address='" + address + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", isLiked=" + isLiked +
            ", isChoosen=" + isChoosen +
            '}';
    }
}