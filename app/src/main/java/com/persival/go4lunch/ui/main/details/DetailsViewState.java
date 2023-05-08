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
    private final Boolean isSelected;
    @NonNull
    private final String nameOfParticipant;
    @NonNull
    private final String avatarUrl;

    public DetailsViewState(@NonNull String id,
                            @NonNull String pictureUrl,
                            @NonNull String name,
                            Float rating,
                            @NonNull String address,
                            @NonNull String phoneNumber,
                            @NonNull String website,
                            @NonNull Boolean isSelected,
                            @NonNull String nameOfParticipant,
                            @NonNull String avatarUrl
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.isSelected = isSelected;
        this.nameOfParticipant = nameOfParticipant;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, rating, address, phoneNumber, website, isSelected, nameOfParticipant, avatarUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DetailsViewState that = (DetailsViewState) o;
        return Float.compare(that.rating, rating) == 0 &&
            id.equals(that.id) &&
            pictureUrl.equals(that.pictureUrl) &&
            name.equals(that.name) &&
            address.equals(that.address) &&
            phoneNumber.equals(that.phoneNumber) &&
            website.equals(that.website) &&
            isSelected.equals(that.isSelected) &&
            nameOfParticipant.equals(that.nameOfParticipant) &&
            avatarUrl.equals(that.avatarUrl);
    }

    @Override
    public String toString() {
        return "DetailsViewState{" +
            "id=" + id +
            "pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", rating=" + rating +
            ", address='" + address + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", isSelected=" + isSelected +
            ", participants='" + nameOfParticipant + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            '}';
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
    public String getPictureUrl() {
        return pictureUrl;
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
    public Boolean getSelected() {
        return isSelected;
    }
    @NonNull
    public String getParticipants() {
        return nameOfParticipant;
    }
    @NonNull
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
