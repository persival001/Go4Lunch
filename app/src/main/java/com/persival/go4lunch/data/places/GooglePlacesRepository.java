package com.persival.go4lunch.data.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.data.places.model.PlaceDetailsResponse;
import com.persival.go4lunch.domain.restaurant.PlacesRepository;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.domain.restaurant.model.PlaceDetailsEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class GooglePlacesRepository implements PlacesRepository {

    private final GooglePlacesApi googlePlacesApi;
    private final LruCache<String, PlaceDetailsResponse> placeDetailsCache = new LruCache<>(512);
    private final LruCache<String, List<NearbyRestaurantsResponse.Place>> placeRestaurantsCache = new LruCache<>(1024);

    @Inject
    public GooglePlacesRepository(GooglePlacesApi googlePlacesApi) {
        this.googlePlacesApi = googlePlacesApi;
    }

    public LiveData<List<NearbyRestaurantsEntity>> getNearbyRestaurants(
        @NonNull String location,
        int radius,
        @NonNull String type,
        @NonNull String apiKey
    ) {
        MutableLiveData<List<NearbyRestaurantsEntity>> restaurantsLiveData = new MutableLiveData<>();

        List<NearbyRestaurantsResponse.Place> cachedRestaurants = placeRestaurantsCache.get(location);

        if (cachedRestaurants == null) {
            googlePlacesApi.getNearbyPlaces(location, radius, type, apiKey).enqueue(new Callback<NearbyRestaurantsResponse>() {
                @Override
                public void onResponse(
                    @NonNull Call<NearbyRestaurantsResponse> call,
                    @NonNull Response<NearbyRestaurantsResponse> response
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                        List<NearbyRestaurantsEntity> nearbyRestaurantsEntity = new ArrayList<>();
                        for (NearbyRestaurantsResponse.Place place : response.body().getResults()) {
                            nearbyRestaurantsEntity.add(mapNearbyRestaurantResponseToEntity(place));
                        }
                        placeRestaurantsCache.put(location, response.body().getResults());
                        restaurantsLiveData.setValue(nearbyRestaurantsEntity);
                    }
                }

                @Override
                public void onFailure(
                    @NonNull Call<NearbyRestaurantsResponse> call,
                    @NonNull Throwable t
                ) {
                    Log.d("FAILURE", "Server failure");
                }
            });
        } else {
            List<NearbyRestaurantsEntity> nearbyRestaurantsEntity = new ArrayList<>();
            for (NearbyRestaurantsResponse.Place place : cachedRestaurants) {
                nearbyRestaurantsEntity.add(mapNearbyRestaurantResponseToEntity(place));
            }
            restaurantsLiveData.setValue(nearbyRestaurantsEntity);
        }
        return restaurantsLiveData;
    }


    public LiveData<PlaceDetailsEntity> getRestaurantLiveData(
        @NonNull String restaurantId,
        @NonNull String apiKey
    ) {
        MutableLiveData<PlaceDetailsEntity> restaurantLiveData = new MutableLiveData<>();

        PlaceDetailsResponse cachedRestaurant = placeDetailsCache.get(restaurantId);

        if (cachedRestaurant == null) {
            googlePlacesApi.getPlaceDetail(restaurantId, apiKey).enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(
                    @NonNull Call<PlaceDetailsResponse> call,
                    @NonNull Response<PlaceDetailsResponse> response
                ) {
                    if (response.isSuccessful() && response.body() != null) {
                        placeDetailsCache.put(restaurantId, response.body());
                        restaurantLiveData.setValue(mapPlaceDetailsResponseToEntity(response.body().getResult()));
                    }
                }

                @Override
                public void onFailure(
                    @NonNull Call<PlaceDetailsResponse> call,
                    @NonNull Throwable t
                ) {
                    Log.d("FAILURE", "Server failure");
                }
            });
        } else {
            restaurantLiveData.setValue(mapPlaceDetailsResponseToEntity(cachedRestaurant.getResult()));
        }

        return restaurantLiveData;
    }

    private NearbyRestaurantsEntity mapNearbyRestaurantResponseToEntity(NearbyRestaurantsResponse.Place place) {
        if (place != null &&
            place.getId() != null &&
            place.getName() != null &&
            place.getAddress() != null
        ) {
            String id = place.getId();
            String name = place.getName();
            String address = place.getAddress();
            boolean openingHours = (place.getOpeningHours() != null) ? place.getOpeningHours().isOpenNow() : false;
            float rating = place.getRating() != null ? place.getRating() : 0;

            List<NearbyRestaurantsResponse.Photo> photos = place.getPhotos();
            List<String> photoUrls = new ArrayList<>();
            if (photos != null) {
                for (NearbyRestaurantsResponse.Photo photo : photos) {
                    photoUrls.add(photo.getPhotoReference());
                }
            }

            double lat = place.getLatitude();
            double lng = place.getLongitude();

            return new NearbyRestaurantsEntity(id, name, address, openingHours, rating, photoUrls, lat, lng);
        } else {
            return null;
        }
    }


    private PlaceDetailsEntity mapPlaceDetailsResponseToEntity(PlaceDetailsResponse.PlaceDetails placeDetails) {
        if (placeDetails != null &&
            placeDetails.getPlaceId() != null &&
            placeDetails.getName() != null &&
            placeDetails.getAddress() != null
        ) {
            String id = placeDetails.getPlaceId();
            String name = placeDetails.getName();
            String address = placeDetails.getAddress();
            float rating = placeDetails.getRating() != null ? placeDetails.getRating() : 0;
            String phoneNumber = placeDetails.getPhoneNumber();
            String website = placeDetails.getWebsite();

            List<PlaceDetailsResponse.PhotoDetails> photos = placeDetails.getPhotos();
            List<String> photoUrls = new ArrayList<>();
            if (photos != null) {
                for (PlaceDetailsResponse.PhotoDetails photo : photos) {
                    photoUrls.add(photo.getPhotoReference());
                }
            }

            return new PlaceDetailsEntity(id, photoUrls, name, rating, address, phoneNumber, website);
        } else {
            return null;
        }
    }
}
