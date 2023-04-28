package com.persival.go4lunch.Data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantEntity {

    @SerializedName("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public static class Place {
        @SerializedName("place_id")
        private String id;
        @SerializedName("name")
        private String name;

        @SerializedName("types")
        private List<String> types;

        @SerializedName("vicinity")
        private String address;

        @SerializedName("opening_hours")
        private OpeningHours openingHours;

        @SerializedName("website")
        private String website;

        @SerializedName("formatted_phone_number")
        private String phoneNumber;

        @SerializedName("photos")
        private List<Photo> photos;

        @SerializedName("rating")
        private float rating;

        public float getRating() {
            return rating * 3 / 5;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getTypes() {
            return types;
        }

        public String getAddress() {
            return address;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public String getWebsite() {
            return website;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

    }

    public static class OpeningHours {
        @SerializedName("open_now")
        private boolean openNow;

        public boolean isOpenNow() {
            return openNow;
        }
    }

    public static class Photo {
        @SerializedName("photo_reference")
        private String photoReference;

        public String getPhotoReference() {
            return photoReference;
        }
    }
}


