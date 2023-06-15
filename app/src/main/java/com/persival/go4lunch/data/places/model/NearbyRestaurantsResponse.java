package com.persival.go4lunch.data.places.model;


import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyRestaurantsResponse {
    @Nullable
    @SerializedName("results")
    private List<Place> results;

    @Nullable
    public List<Place> getResults() {
        return results;
    }


    public static class Place {
        @Nullable
        @SerializedName("geometry")
        private Geometry geometry;

        @Nullable
        @SerializedName("place_id")
        private String placeId;

        @Nullable
        @SerializedName("name")
        private String name;

        @Nullable
        @SerializedName("vicinity")
        private String address;

        @Nullable
        @SerializedName("opening_hours")
        private OpeningHours openingHours;

        @Nullable
        @SerializedName("website")
        private String website;

        @Nullable
        @SerializedName("formatted_phone_number")
        private String phoneNumber;

        @Nullable
        @SerializedName("photos")
        private List<Photo> photos;

        @Nullable
        @SerializedName("rating")
        private Float rating;


        public double getLatitude() {
            return geometry != null && geometry.location != null ? geometry.location.lat : 0.0;
        }

        public double getLongitude() {
            return geometry != null && geometry.location != null ? geometry.location.lng : 0.0;
        }

        @Nullable
        public String getId() {
            return placeId;
        }

        @Nullable
        public Float getRating() {
            return rating;
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
        public OpeningHours getOpeningHours() {
            return openingHours;
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
        public List<Photo> getPhotos() {
            return photos;
        }
    }


    public static class OpeningHours {
        @Nullable
        @SerializedName("open_now")
        private Boolean openNow;

        public Boolean isOpenNow() {
            return openNow;
        }
    }


    public static class Photo {
        @Nullable
        @SerializedName("photo_reference")
        private String photoReference;

        @Nullable
        public String getPhotoReference() {
            return photoReference;
        }
    }


    private static class Geometry {
        @Nullable
        @SerializedName("location")
        private Location location;

        private static class Location {
            @SerializedName("lat")
            private double lat;
            @SerializedName("lng")
            private double lng;
        }
    }
}


