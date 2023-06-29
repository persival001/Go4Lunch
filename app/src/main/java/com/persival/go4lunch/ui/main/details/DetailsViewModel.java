package com.persival.go4lunch.ui.main.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.details.GetLikedRestaurantsUseCase;
import com.persival.go4lunch.domain.details.GetRestaurantDetailsUseCase;
import com.persival.go4lunch.domain.details.SetLikedRestaurantUseCase;
import com.persival.go4lunch.domain.details.SetRestaurantChosenToEatUseCase;
import com.persival.go4lunch.domain.details.model.NotificationEntity;
import com.persival.go4lunch.domain.notifications.CancelRestaurantNotificationUseCase;
import com.persival.go4lunch.domain.notifications.ScheduleRestaurantNotificationUseCase;
import com.persival.go4lunch.domain.restaurant.model.PlaceDetailsEntity;
import com.persival.go4lunch.domain.user.GetLoggedUserUseCase;
import com.persival.go4lunch.domain.user.model.LoggedUserEntity;
import com.persival.go4lunch.domain.workmate.GetWorkmatesEatAtRestaurantUseCase;
import com.persival.go4lunch.domain.workmate.model.UserEatAtRestaurantEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    public final LiveData<DetailsRestaurantViewState> restaurantViewStateLiveData;
    public final LiveData<List<String>> likedRestaurantsLiveData;
    public final LiveData<List<DetailsWorkmateViewState>> workmatesViewStateLiveData;
    public final MediatorLiveData<NotificationEntity> notificationLiveData;
    public final MutableLiveData<List<String>> workmatesGoToThisRestaurantLiveData;
    @NonNull
    private final Resources resources;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantLiked;
    @NonNull
    private final MutableLiveData<Boolean> isRestaurantChosenLiveData;
    @NonNull
    private final SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase;
    @NonNull
    private final SetLikedRestaurantUseCase setLikedRestaurantUseCase;
    @NonNull
    private final GetLoggedUserUseCase getLoggedUserUseCase;
    @NonNull
    private final ScheduleRestaurantNotificationUseCase scheduleRestaurantNotificationUseCase;
    @NonNull
    private final CancelRestaurantNotificationUseCase cancelRestaurantNotificationUseCase;

    private DetailsRestaurantViewState detailsRestaurantViewState;

    @Inject
    public DetailsViewModel(
        @NonNull Resources resources,
        @NonNull GetRestaurantDetailsUseCase getRestaurantDetailsUseCase,
        @NonNull SavedStateHandle savedStateHandle,
        @NonNull SetRestaurantChosenToEatUseCase setRestaurantChosenToEatUseCase,
        @NonNull GetLikedRestaurantsUseCase getLikedRestaurantsUseCase,
        @NonNull SetLikedRestaurantUseCase setLikedRestaurantUseCase,
        @NonNull GetWorkmatesEatAtRestaurantUseCase getWorkmatesEatAtRestaurantUseCase,
        @NonNull GetLoggedUserUseCase getLoggedUserUseCase,
        @NonNull ScheduleRestaurantNotificationUseCase scheduleRestaurantNotificationUseCase,
        @NonNull CancelRestaurantNotificationUseCase cancelRestaurantNotificationUseCase
    ) {
        this.resources = resources;
        this.setRestaurantChosenToEatUseCase = setRestaurantChosenToEatUseCase;
        this.setLikedRestaurantUseCase = setLikedRestaurantUseCase;
        this.getLoggedUserUseCase = getLoggedUserUseCase;
        this.scheduleRestaurantNotificationUseCase = scheduleRestaurantNotificationUseCase;
        this.cancelRestaurantNotificationUseCase = cancelRestaurantNotificationUseCase;
        isRestaurantLiked = new MutableLiveData<>();
        isRestaurantLiked.setValue(false);
        isRestaurantChosenLiveData = new MutableLiveData<>();
        isRestaurantChosenLiveData.setValue(false);
        workmatesGoToThisRestaurantLiveData = new MutableLiveData<>();

        String restaurantId = savedStateHandle.get(DetailsFragment.KEY_RESTAURANT_ID);

        if (restaurantId == null) {
            throw new IllegalStateException("Please use DetailsFragment.newInstance() to launch the Fragment");
        }

        likedRestaurantsLiveData = getLikedRestaurantsUseCase.invoke();

        workmatesViewStateLiveData = Transformations.map(getWorkmatesEatAtRestaurantUseCase.invoke(), users -> {
            List<DetailsWorkmateViewState> detailsWorkmateViewState = new ArrayList<>();
            List<String> workmatesGoToThisRestaurant = new ArrayList<>();
            LoggedUserEntity loggedUser = this.getLoggedUserUseCase.invoke();
            String loggedInUserId = loggedUser != null ? loggedUser.getId() : null;

            for (UserEatAtRestaurantEntity userEatAtRestaurantEntity : users) {
                if (userEatAtRestaurantEntity != null &&
                    userEatAtRestaurantEntity.getRestaurantId() != null &&
                    userEatAtRestaurantEntity.getRestaurantId().equals(restaurantId)) {

                    workmatesGoToThisRestaurant.add(userEatAtRestaurantEntity.getName());

                    detailsWorkmateViewState.add(
                        new DetailsWorkmateViewState(
                            userEatAtRestaurantEntity.getId(),
                            userEatAtRestaurantEntity.getPictureUrl(),
                            getWorkmateNameIsJoining(userEatAtRestaurantEntity.getName())
                        )
                    );

                    if (userEatAtRestaurantEntity.getId().equals(loggedInUserId)) {
                        isRestaurantChosenLiveData.setValue(true);
                    }
                }
            }

            workmatesGoToThisRestaurantLiveData.setValue(workmatesGoToThisRestaurant);

            return detailsWorkmateViewState;
        });

        restaurantViewStateLiveData = Transformations.map(
            getRestaurantDetailsUseCase.invoke(restaurantId),
            restaurant -> {
                detailsRestaurantViewState = new DetailsRestaurantViewState(
                    restaurant.getId(),
                    getPictureUrl(restaurant.getPhotoUrl()),
                    getFormattedName(restaurant.getName()),
                    getRating(restaurant.getRating()),
                    restaurant.getAddress(),
                    restaurant.getPhoneNumber(),
                    restaurant.getWebsite()
                );
                return detailsRestaurantViewState;
            }
        );

        notificationLiveData = new MediatorLiveData<>();
        LiveData<PlaceDetailsEntity> restaurantLiveData = getRestaurantDetailsUseCase.invoke(restaurantId);
        LiveData<List<String>> workmatesLiveData = workmatesGoToThisRestaurantLiveData;

        notificationLiveData.addSource(restaurantLiveData, restaurant -> {
            if (restaurant != null && workmatesLiveData.getValue() != null) {
                notificationLiveData.setValue(mapNotificationEntity(restaurant, workmatesLiveData.getValue()));
            }
        });

        notificationLiveData.addSource(workmatesLiveData, workmates -> {
            if (workmates != null && restaurantLiveData.getValue() != null) {
                notificationLiveData.setValue(mapNotificationEntity(restaurantLiveData.getValue(), workmates));
            }
        });

    }

    private NotificationEntity mapNotificationEntity(
        PlaceDetailsEntity restaurant,
        List<String> workmates
    ) {

        String workmateNamesString = String.join(", ", workmates);

        return new NotificationEntity(restaurant.getName(), restaurant.getAddress(), workmateNamesString);
    }

    public LiveData<Boolean> getIsRestaurantLiked() {
        return isRestaurantLiked;
    }

    public LiveData<NotificationEntity> getNotificationLiveData() {
        return notificationLiveData;
    }

    public void onChooseRestaurant(DetailsRestaurantViewState detail) {
        // Toggle the chosen state
        if (isRestaurantChosenLiveData.getValue() != null) {
            isRestaurantChosenLiveData.setValue(!isRestaurantChosenLiveData.getValue());
        }

        // Check if the restaurant is chosen
        if (isRestaurantChosenLiveData.getValue() != null && isRestaurantChosenLiveData.getValue()) {
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
            setLikedRestaurantUseCase.invoke(
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

    public void updateIsRestaurantLiked(boolean isLiked) {
        isRestaurantLiked.setValue(isLiked);
    }

    public LiveData<Boolean> getIsRestaurantChosenLiveData() {
        return isRestaurantChosenLiveData;
    }

    public void scheduleRestaurantNotification(String restaurantName, String restaurantAddress, String workmates) {
        scheduleRestaurantNotificationUseCase.invoke(restaurantName, restaurantAddress, workmates);
    }

    public void cancelRestaurantNotification() {
        cancelRestaurantNotificationUseCase.invoke();
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
    public String getPictureUrl(List<String> photos) {
        if (photos != null && !photos.isEmpty()) {
            String photoReference = photos.get(0);
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference + "&key=" + MAPS_API_KEY;
        } else {
            return "";
        }
    }

}



