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
    private final float rating;
    @NonNull
    private final String typeOfCuisineAndAddress;
    @NonNull
    private final String phoneNumber;
    @NonNull
    private final String website;
    @NonNull
    private Boolean isSelected;
    @NonNull
    private String participants;
    @NonNull
    private String avatarUrl;

    public DetailsViewState(@NonNull String id,
                            @NonNull String pictureUrl,
                            @NonNull String name,
                            float rating,
                            @NonNull String typeOfCuisineAndAddress,
                            @NonNull String phoneNumber,
                            @NonNull String website,
                            @NonNull Boolean isSelected,
                            @NonNull String participants,
                            @NonNull String avatarUrl
    ) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.rating = rating;
        this.typeOfCuisineAndAddress = typeOfCuisineAndAddress;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.isSelected = isSelected;
        this.participants = participants;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureUrl, name, rating, typeOfCuisineAndAddress, phoneNumber, website, isSelected, participants, avatarUrl);
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
            typeOfCuisineAndAddress.equals(that.typeOfCuisineAndAddress) &&
            phoneNumber.equals(that.phoneNumber) &&
            website.equals(that.website) &&
            isSelected.equals(that.isSelected) &&
            participants.equals(that.participants) &&
            avatarUrl.equals(that.avatarUrl);
    }

    @Override
    public String toString() {
        return "DetailsViewState{" +
            "id=" + id +
            "pictureUrl='" + pictureUrl + '\'' +
            ", name='" + name + '\'' +
            ", rating=" + rating +
            ", typeOfCuisineAndAddress='" + typeOfCuisineAndAddress + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", isSelected=" + isSelected +
            ", participants='" + participants + '\'' +
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

    public float getRating() {
        return rating;
    }

    @NonNull
    public String getTypeOfCuisineAndAddress() {
        return typeOfCuisineAndAddress;
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
        return participants;
    }

    @NonNull
    public String getAvatarUrl() {
        return avatarUrl;
    }

}
