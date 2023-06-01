package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.data.places.GooglePlacesRepository;
import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    private final GooglePlacesRepository googlePlacesRepository;
    private final MutableLiveData<List<DetailsUserViewState>> userListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRestaurantLiked;
    private final MutableLiveData<Boolean> isRestaurantChosen;
    private DetailsViewState restaurantDetail;

    @Inject
    public DetailsViewModel(
        @NonNull final GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
        isRestaurantLiked = new MutableLiveData<>();
        isRestaurantLiked.setValue(false);
        isRestaurantChosen = new MutableLiveData<>();
        isRestaurantChosen.setValue(false);
    }

    public LiveData<Boolean> getIsRestaurantChosen() {
        return isRestaurantChosen;
    }

    public LiveData<Boolean> getIsRestaurantLiked() {
        return isRestaurantLiked;
    }

    public void chooseThisRestaurant(DetailsViewState detail) {
        this.restaurantDetail = detail;
        if (isRestaurantChosen.getValue() != null) {
            isRestaurantChosen.setValue(!isRestaurantChosen.getValue());
        }
        if (restaurantDetail != null) {
            //restaurantDetail.setChosen(isRestaurantChosen.getValue());
        }
    }

    public LiveData<List<DetailsUserViewState>> getUserLiveData() {
        List<DetailsUserViewState> users = new ArrayList<>();
        users.add(new DetailsUserViewState("1", "Ginette is eating at Mac Mickey", "https://picsum.photos/200"));
        users.add(new DetailsUserViewState("2", "Jean is eating at The rest O' rent", "https://picsum.photos/200"));
        userListLiveData.setValue(users);
        return userListLiveData;
    }

    public LiveData<DetailsViewState> getDetailViewStateLiveData(String restaurantId) {
        return Transformations.map(
            googlePlacesRepository.getRestaurantLiveData(restaurantId, MAPS_API_KEY),

            restaurant -> {
                restaurantDetail = new DetailsViewState(
                    restaurant.getId() != null ? restaurant.getId() : "",
                    getPictureUrl(restaurant.getPhotos()) != null ? getPictureUrl(restaurant.getPhotos()) : "",
                    getFormattedName(restaurant.getName()),
                    getRating(restaurant.getRating()),
                    restaurant.getAddress() != null ? restaurant.getAddress() : "",
                    restaurant.getPhoneNumber() != null ? restaurant.getPhoneNumber() : "",
                    restaurant.getWebsite() != null ? restaurant.getWebsite() : "",
                    isRestaurantLiked.getValue() != null && isRestaurantLiked.getValue(),
                    isRestaurantChosen.getValue() != null && isRestaurantChosen.getValue()
                );
                return restaurantDetail;
            }
        );
    }

    public void toggleLike() {
        if (isRestaurantLiked.getValue() != null) {
            isRestaurantLiked.setValue(!isRestaurantLiked.getValue());
        }
        if (restaurantDetail != null) {
            //restaurantDetail.setLiked(isRestaurantLiked.getValue());
        }
    }

    // Convert rating from 5 to 3 stars
    private float getRating(Float rating) {
        if (rating != null) {
            return rating * 3F / 5F;
        } else {
            return 0F;
        }
    }

    private String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    private String getFormattedName(String name) {
        if (name != null) {
            String lowercaseName = name.toLowerCase();
            return toTitleCase(lowercaseName);
        } else {
            return "";
        }
    }

    // Get a photo reference if it exists and convert it to a picture url
    public String getPictureUrl(List<NearbyRestaurantsResponse.Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0).getPhotoReference();
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "";
        }
    }
}


