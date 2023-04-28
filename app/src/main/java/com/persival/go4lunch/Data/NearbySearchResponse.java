package com.persival.go4lunch.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbySearchResponse {

    @SerializedName("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }

    public static class OpeningHours {
        @SerializedName("open_now")
        private boolean openNow;

        public boolean isOpenNow() {
            return openNow;
        }

        public void setOpenNow(boolean openNow) {
            this.openNow = openNow;
        }
    }

    public static class Photo {
        @SerializedName("photo_reference")
        private String photoReference;

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }
    }
}

