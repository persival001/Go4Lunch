package com.persival.go4lunch.ui.mainactivity.details;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.persival.go4lunch.Data.model.RestaurantEntity;
import com.persival.go4lunch.repository.Repository;
import com.persival.go4lunch.ui.mainactivity.restaurants.RestaurantsViewState;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends ViewModel {
    private final Repository repository;

    private final MutableLiveData<List<DetailsViewState>> detailRestaurantLiveData = new MutableLiveData<>();

    public DetailsViewModel(
        @NonNull final Repository repository
    ) {
        this.repository = repository;
    }

    public LiveData<List<DetailsViewState>> getDetailRestaurantLiveData() {
        return detailRestaurantLiveData;
    }

    public void getRestaurantDetailsViewStateLiveData(long restaurantId) {
        repository.getRestaurantLiveData(restaurantId).observeForever(restaurant -> {
            List<DetailsViewState> restaurantsList = new ArrayList<>();
            if (restaurant != null) {
                restaurantsList.add(new DetailsViewState(
                    restaurant.getId(),
                    restaurant.getPictureUrl(),
                    restaurant.getName(),
                    restaurant.getRating(),
                    restaurant.getTypeOfCuisineAndAddress(),
                    restaurant.getPhoneNumber(),
                    restaurant.getWebsite(),
                    true,
                    "(2)",
                    "https://pravatar.cc/150?img=3"
                ));
            }
            detailRestaurantLiveData.setValue(restaurantsList);
        });
    }
}

