package com.persival.go4lunch.utils;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;

import java.util.List;

public class ConversionUtils {

    public static float getRating(Float rating) {
        return rating * 3F / 5F;
    }

    public static String getOpeningTime(NearbyRestaurantsResponse.OpeningHours openingHours) {
        if (openingHours != null && openingHours.isOpenNow()) {
            return "Open";
        } else {
            return "Closed";
        }
    }

    public static String getPictureUrl(List<NearbyRestaurantsResponse.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "https://unsplash.com/fr/photos/5dsZnCVDHd0";
        }
    }
}




