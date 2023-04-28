package com.persival.go4lunch.ui.mainactivity.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;

import com.persival.go4lunch.Data.model.RestaurantEntity;

import java.util.List;
import java.util.Objects;

public class RestaurantsViewState {

    private long id;

    @NonNull
    private final String name;

    @NonNull
    private final String typeOfCuisineAndAddress;

    @NonNull
    private final String openingTime;

    @NonNull
    private final String distance;

    @NonNull
    private final String participants;

    @NonNull
    private final float rating;

    @NonNull
    private final String pictureUrl;

    public RestaurantsViewState(@NonNull RestaurantEntity.Place place) {
        this.id = longId(place.getId());
        this.name = place.getName();
        this.typeOfCuisineAndAddress = buildTypeOfCuisineAndAddress(place.getTypes(), place.getAddress());
        this.openingTime = buildOpeningTime(place.getOpeningHours());
        this.distance = "100m"; // Calculer la distance en fonction de la position de l'utilisateur
        this.participants = "(2)"; // Déterminer les participants
        this.rating = place.getRating();
        this.pictureUrl = buildPictureUrl(place.getPhotos());
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTypeOfCuisineAndAddress() {
        return typeOfCuisineAndAddress;
    }

    @NonNull
    public String getOpeningTime() {
        return openingTime;
    }

    @NonNull
    public String getDistance() {
        return distance;
    }

    @NonNull
    public String getParticipants() {
        return participants;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @NonNull
    public float getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            typeOfCuisineAndAddress,
            openingTime,
            distance,
            participants,
            pictureUrl,
            rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RestaurantsViewState that = (RestaurantsViewState) o;
        return id == that.id
            && name.equals(that.name)
            && typeOfCuisineAndAddress.equals(that.typeOfCuisineAndAddress)
            && openingTime.equals(that.openingTime)
            && distance.equals(that.distance)
            && participants.equals(that.participants)
            && pictureUrl.equals(that.pictureUrl)
            && rating == that.rating;
    }

    private long longId(String stringId) {
        return Long.parseLong(stringId);
    }
    private String buildTypeOfCuisineAndAddress(List<String> types, String address) {
        return types.get(0) + " - " + address;
    }

    private String buildOpeningTime(RestaurantEntity.OpeningHours openingHours) {
        if (openingHours != null && openingHours.isOpenNow()) {
            return "Open now";
        } else {
            return "Closed";
        }
    }

    private String buildPictureUrl(List<RestaurantEntity.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "https://unsplash.com/fr/photos/5dsZnCVDHd0";
        }
    }
}
