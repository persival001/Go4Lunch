package com.persival.go4lunch.ui.mainactivity.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;

import com.persival.go4lunch.Data.NearbySearchResponse;

import java.util.List;
import java.util.Objects;

public class RestaurantsViewState {

    private long id = 1;

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

    //@NonNull
    //private final int stars;

    @NonNull
    private final String pictureUrl;

    public RestaurantsViewState(@NonNull NearbySearchResponse.Place place) {
        this.id = id;
        this.name = place.getName();
        this.typeOfCuisineAndAddress = buildTypeOfCuisineAndAddress(place.getTypes(), place.getAddress());
        this.openingTime = buildOpeningTime(place.getOpeningHours());
        this.distance = ""; // Vous devez calculer la distance en fonction de la position de l'utilisateur
        this.participants = ""; // Vous devez déterminer les participants
        //this.stars = ...; // Vous devez déterminer les étoiles si vous souhaitez les utiliser
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

    @Override
    public int hashCode() {
        return Objects.hash(id, name, typeOfCuisineAndAddress, openingTime, distance, participants, pictureUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RestaurantsViewState that = (RestaurantsViewState) o;
        return id == that.id
            //&& stars == that.stars
            && name.equals(that.name)
            && typeOfCuisineAndAddress.equals(that.typeOfCuisineAndAddress)
            && openingTime.equals(that.openingTime)
            && distance.equals(that.distance)
            && participants.equals(that.participants)
            && pictureUrl.equals(that.pictureUrl);
    }

    private String buildTypeOfCuisineAndAddress(List<String> types, String address) {
        // Vous devez implémenter la logique pour construire la chaîne "typeOfCuisineAndAddress"
        // en fonction des types et de l'adresse
        return "Type of cuisine - " + address;
    }

    private String buildOpeningTime(NearbySearchResponse.OpeningHours openingHours) {
        if (openingHours != null && openingHours.isOpenNow()) {
            return "Open now";
        } else {
            return "Closed";
        }
    }

    private String buildPictureUrl(List<NearbySearchResponse.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            // Vous devez construire l'URL de l'image en utilisant la référence photo
            // et la clé API Google Places (si nécessaire)
            return "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photoReference + MAPS_API_KEY;
        } else {
            return ""; // Fournir une URL d'image par défaut ou laisser vide
        }
    }
}
