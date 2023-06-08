package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.details.GetRestaurantDetailsUseCase;
import com.persival.go4lunch.domain.details.GetWorkmatesListUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    public final LiveData<List<DetailsWorkmateViewState>> workmatesViewStateLiveData;
    public final LiveData<DetailsRestaurantViewState> restaurantViewStateLiveData;
    @NonNull
    private final Application context;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantLiked;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantChosen;
    private DetailsRestaurantViewState restaurantDetail;


    @Inject
    public DetailsViewModel(
        @NonNull Application context,
        @NonNull GetWorkmatesListUseCase getWorkmatesListUseCase,
        @NonNull GetRestaurantDetailsUseCase getRestaurantDetailsUseCase,
        @NonNull SavedStateHandle savedStateHandle
    ) {
        this.context = context;
        isRestaurantLiked = new MutableLiveData<>();
        isRestaurantLiked.setValue(false);
        isRestaurantChosen = new MutableLiveData<>();
        isRestaurantChosen.setValue(false);

        String restaurantId = savedStateHandle.get(DetailsFragment.KEY_RESTAURANT_ID);

        if (restaurantId == null) {
            throw new IllegalStateException("Please use DetailsFragment.newInstance() to launch the Fragment");
        }

        workmatesViewStateLiveData = Transformations.map(
            getWorkmatesListUseCase.invoke(restaurantId),
            workmates -> {
                List<DetailsWorkmateViewState> mappedWorkmates = new ArrayList<>();
                for (WorkmateEntity workmate : workmates) {
                    DetailsWorkmateViewState restaurantDetail = new DetailsWorkmateViewState(
                        workmate.getId(),
                        workmate.getPictureUrl(),
                        getWorkmateNameIsJoining(workmate.getName())
                    );
                    mappedWorkmates.add(restaurantDetail);
                }
                return mappedWorkmates;
            }
        );

        restaurantViewStateLiveData = Transformations.map(
            getRestaurantDetailsUseCase.invoke(restaurantId),
            restaurant -> {
                if (restaurant.getId() != null &&
                    restaurant.getName() != null &&
                    restaurant.getAddress() != null
                ) {
                    return new DetailsRestaurantViewState(
                        restaurant.getId(),
                        getPictureUrl(restaurant.getPhotos()),
                        getFormattedName(restaurant.getName()),
                        getRating(restaurant.getRating()),
                        restaurant.getAddress(),
                        restaurant.getPhoneNumber(),
                        restaurant.getWebsite()
                    );
                } else {
                    return null;
                }
            }
        );
    }

    public LiveData<Boolean> getIsRestaurantChosen() {
        return isRestaurantChosen;
    }

    public LiveData<Boolean> getIsRestaurantLiked() {
        return isRestaurantLiked;
    }

    public void chooseThisRestaurant(DetailsRestaurantViewState detail) {
        this.restaurantDetail = detail;
        if (isRestaurantChosen.getValue() != null) {
            isRestaurantChosen.setValue(!isRestaurantChosen.getValue());
        }
        if (restaurantDetail != null) {
            //restaurantDetail.setChosen(isRestaurantChosen.getValue());
        }
    }

    public void toggleLike() {
        if (isRestaurantLiked.getValue() != null) {
            isRestaurantLiked.setValue(!isRestaurantLiked.getValue());
        }
        if (restaurantDetail != null) {
            //restaurantDetail.setLiked(isRestaurantLiked.getValue());
        }
    }

    public LiveData<List<DetailsWorkmateViewState>> getWorkmateListLiveData() {
        return workmatesViewStateLiveData;
    }

    public LiveData<DetailsRestaurantViewState> getDetailViewStateLiveData() {
        return restaurantViewStateLiveData;
    }

    // Add "is joining!" after the workmate name
    private String getWorkmateNameIsJoining(@NonNull String name) {
        return context.getString(R.string.is_joining, name);
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