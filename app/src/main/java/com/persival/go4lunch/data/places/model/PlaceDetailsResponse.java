package com.persival.go4lunch.data.places.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetailsResponse {

    @SerializedName("result")
    private PlaceDetails result;

    @Nullable
    public PlaceDetails getResult() {
        return result;
    }

    public static class PlaceDetails {
        @Nullable
        @SerializedName("place_id")
        private String placeId;

        @Nullable
        @SerializedName("name")
        private String name;

        @Nullable
        @SerializedName("formatted_address")
        private String address;

        @Nullable
        @SerializedName("website")
        private String website;

        @Nullable
        @SerializedName("formatted_phone_number")
        private String phoneNumber;

        @Nullable
        @SerializedName("photos")
        private List<PhotoDetails> photos;

        @Nullable
        @SerializedName("rating")
        private Float rating;

        @Nullable
        public String getPlaceId() {
            return placeId;
        }

        @Nullable
        public String getName() {
            return name;
        }

        @Nullable
        public String getAddress() {
            return address;
        }

        @Nullable
        public String getWebsite() {
            return website;
        }

        @Nullable
        public String getPhoneNumber() {
            return phoneNumber;
        }

        @Nullable
        public List<PhotoDetails> getPhotos() {
            return photos;
        }

        @Nullable
        public Float getRating() {
            return rating;
        }
    }

    public static class PhotoDetails {
        @Nullable
        @SerializedName("photo_reference")
        private String photoReference;

        @Nullable
        public String getPhotoReference() {
            return photoReference;
        }
    }
}


