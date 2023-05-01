package com.persival.go4lunch.Data.model;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

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
        private String placeId;
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

        public String getId() {
            return placeId;
        }

        public float getRating() {
            return rating * 3 / 5;
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

        public String getTypeOfCuisineAndAddress() {
            return types.get(0) + " - " + address;
        }

        public String getPictureUrl() {
            if (photos != null && !photos.isEmpty()) {
                String photoReference = photos.get(0).getPhotoReference();
                return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    photoReference + "&key=" + MAPS_API_KEY;
            } else {
                return "https://unsplash.com/fr/photos/5dsZnCVDHd0";
            }
        }

        public String getOpeningTime() {
            if (openingHours != null && openingHours.isOpenNow()) {
                return "Open now";
            } else {
                return "Closed";
            }
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


