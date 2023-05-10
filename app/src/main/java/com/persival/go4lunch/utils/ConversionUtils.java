package com.persival.go4lunch.utils;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;

import java.util.List;
import java.util.Locale;

public class ConversionUtils {

    private ConversionUtils() {
        // This class is not meant to be instantiated
    }

    // Convert rating from 5 to 3 stars
    public static float getRating(Float rating) {
        if (rating != null) {
            return rating * 3F / 5F;
        } else {
            return 0F;
        }
    }

    // Convert boolean to string "Open" or "Closed"
    public static String getOpeningTime(NearbyRestaurantsResponse.OpeningHours openingHours) {
        if (openingHours != null && openingHours.isOpenNow()) {
            return "Open";
        } else {
            return "Closed";
        }
    }

    // Get a photo reference if it exists and convert it to a picture url
    public static String getPictureUrl(List<NearbyRestaurantsResponse.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "https://picsum.photos/200";
        }
    }

    // Get the distance between two points
    public static String getHaversineDistance(double lat1, double lon1, String latLng) {
        if (latLng == null || latLng.isEmpty()) {
            return "0";
        }

        String[] latLngArray = latLng.split(",");
        if (latLngArray.length != 2) {
            return "0";
        }

        double lat2;
        double lon2;

        try {
            lat2 = Double.parseDouble(latLngArray[0]);
            lon2 = Double.parseDouble(latLngArray[1]);
        } catch (NumberFormatException e) {
            return "0";
        }

        final int R = 6371_000; // Radius of the earth in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return String.format(Locale.getDefault(), "%.0f", distance) + " m";
    }

}




