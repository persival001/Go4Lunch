package com.persival.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.persival.go4lunch.data.repository.GooglePlacesRepository;
import com.persival.go4lunch.ui.main.details.DetailsViewModel;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel;
import com.persival.go4lunch.ui.main.userlist.UserListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(
                        new GooglePlacesRepository()
                    );
                }
            }
        }

        return factory;
    }

    @NonNull
    private final GooglePlacesRepository googlePlacesRepository;

    private ViewModelFactory(
        @NonNull GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
        // TODO Persival use FusedLocationProviderClient for LocationRepository
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel(
                googlePlacesRepository
            );
        } else if (modelClass.isAssignableFrom(UserListViewModel.class)) {
            return (T) new UserListViewModel(
                googlePlacesRepository
            );
        } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(
                googlePlacesRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}