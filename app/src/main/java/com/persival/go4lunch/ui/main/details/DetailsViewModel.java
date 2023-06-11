package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.domain.details.GetLikedRestaurantsUseCase;
import com.persival.go4lunch.domain.details.GetRestaurantChosenToEatUseCase;
import com.persival.go4lunch.domain.details.GetRestaurantDetailsUseCase;
import com.persival.go4lunch.domain.details.GetWorkmatesListUseCase;
import com.persival.go4lunch.domain.details.SetRestaurantChosenToEatUseCase;
import com.persival.go4lunch.domain.details.SetRestaurantLikedUseCase;
import com.persival.go4lunch.domain.workmate.model.WorkmateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    public final LiveData<List<DetailsWorkmateViewState>> workmatesViewStateLiveData;
    public final LiveData<DetailsRestaurantViewState> restaurantViewStateLiveData;
    public final LiveData<List<String>> likedRestaurantsLiveData;
    @NonNull
    private final Resources resources;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantLiked;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantChosen;
    @NonNull
    private final SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase;
    @NonNull
    private final SetRestaurantLikedUseCase setRestaurantLikedUseCase;
    @NonNull
    private final GetRestaurantChosenToEatUseCase getRestaurantChosenToEatUseCase;
    public LiveData<Boolean> isRestaurantChosenLiveData;
    private DetailsRestaurantViewState detailsRestaurantViewState;

    @Inject
    public DetailsViewModel(
        @NonNull Resources resources,
        @NonNull GetWorkmatesListUseCase getWorkmatesListUseCase,
        @NonNull GetRestaurantDetailsUseCase getRestaurantDetailsUseCase,
        @NonNull SavedStateHandle savedStateHandle,
        @NonNull SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase,
        @NonNull GetLikedRestaurantsUseCase getLikedRestaurantsUseCase,
        @NonNull SetRestaurantLikedUseCase setRestaurantLikedUseCase,
        @NonNull GetRestaurantChosenToEatUseCase getRestaurantChosenToEatUseCase) {
        this.resources = resources;
        this.setRestaurantChosenToEatUseCase = setRestaurantChosenToEatUseCase;
        this.setRestaurantLikedUseCase = setRestaurantLikedUseCase;
        this.getRestaurantChosenToEatUseCase = getRestaurantChosenToEatUseCase;
        isRestaurantLiked = new MutableLiveData<>();
        isRestaurantLiked.setValue(false);
        isRestaurantChosen = new MutableLiveData<>();
        isRestaurantChosen.setValue(false);

        String restaurantId = savedStateHandle.get(DetailsFragment.KEY_RESTAURANT_ID);

        if (restaurantId == null) {
            throw new IllegalStateException("Please use DetailsFragment.newInstance() to launch the Fragment");
        }
        likedRestaurantsLiveData = getLikedRestaurantsUseCase.invoke();

        isRestaurantChosenLiveData = getRestaurantChosenToEatUseCase.invoke(restaurantId);

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
                    detailsRestaurantViewState = new DetailsRestaurantViewState(
                        restaurant.getId(),
                        getPictureUrl(restaurant.getPhotos()),
                        getFormattedName(restaurant.getName()),
                        getRating(restaurant.getRating()),
                        restaurant.getAddress(),
                        restaurant.getPhoneNumber(),
                        restaurant.getWebsite()
                    );
                    return detailsRestaurantViewState;
                } else {
                    detailsRestaurantViewState = null;
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

    public void onChooseRestaurant(DetailsRestaurantViewState detail) {
        // Toggle the chosen state
        if (isRestaurantChosen.getValue() != null) {
            isRestaurantChosen.setValue(!isRestaurantChosen.getValue());
        }

        // Check if the restaurant is chosen
        if (isRestaurantChosen.getValue() != null && isRestaurantChosen.getValue()) {
            // Restaurant is chosen - update detailsRestaurantViewState, restaurantId and restaurantName
            this.detailsRestaurantViewState = detail;
            setRestaurantChosenToEatUseCase.invoke(
                detailsRestaurantViewState.getId(),
                detailsRestaurantViewState.getRestaurantName()
            );
        } else {
            // Restaurant is unchosen - set everything to null
            this.detailsRestaurantViewState = null;
            setRestaurantChosenToEatUseCase.invoke(null, null);
        }
    }

    public void onToggleLikeRestaurant() {
        // Toggle the like state
        if (isRestaurantLiked.getValue() != null) {
            isRestaurantLiked.setValue(!isRestaurantLiked.getValue());
        }
        // Check if the restaurant is liked
        if (detailsRestaurantViewState != null) {
            setRestaurantLikedUseCase.invoke(
                isRestaurantLiked.getValue() != null &&
                    isRestaurantLiked.getValue(),
                detailsRestaurantViewState.getId()
            );
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
        return resources.getString(R.string.is_joining, name);
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

    public void updateIsRestaurantLiked(boolean isLiked) {
        isRestaurantLiked.setValue(isLiked);
    }

    public void updateIsRestaurantChosen(boolean isChosen) {
        isRestaurantLiked.setValue(isChosen);
    }

}



