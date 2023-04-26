package com.persival.go4lunch.Data;

import com.persival.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class PlaceToRestaurantMapper {
    public static List<Restaurant> map(List<NearbySearchResponse.Place> places) {
        List<Restaurant> restaurants = new ArrayList<>();

        for (NearbySearchResponse.Place place : places) {
            long id = place.getId();
            String name = place.getName();
            String address = place.getAddress();
            String photoUrl = ""; // À remplacer par la logique pour récupérer l'URL de la photo à partir des données de l'API Google Places.
            String openingHours = place.getOpeningHours() != null && place.getOpeningHours().isOpenNow() ? "Open now" : "Closed";
            String distance = ""; // À remplacer par la logique pour calculer la distance entre l'utilisateur et le restaurant.
            String workmates = ""; // À remplacer par la logique pour récupérer le nombre de collègues qui déjeunent dans ce restaurant.

            Restaurant restaurant = new Restaurant(id, name, address, photoUrl, openingHours, distance, workmates);
            restaurants.add(restaurant);
        }

        return restaurants;
    }
}
