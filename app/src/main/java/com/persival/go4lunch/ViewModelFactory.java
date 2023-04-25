package com.persival.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.persival.go4lunch.repository.Repository;
import com.persival.go4lunch.ui.mainactivity.details.DetailsViewModel;
import com.persival.go4lunch.ui.mainactivity.restaurants.RestaurantsViewModel;
import com.persival.go4lunch.ui.mainactivity.userlist.UserListViewModel;

import java.time.Clock;
import java.time.format.DateTimeFormatter;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(
                        new Repository()
                    );
                }
            }
        }

        return factory;
    }

    @NonNull
    private final Repository repository;

    private ViewModelFactory(
        @NonNull Repository repository
    ) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel(
                MainApplication.getInstance().getResources(),
                repository
            );
        } else if (modelClass.isAssignableFrom(UserListViewModel.class)) {
            return (T) new UserListViewModel(
                MainApplication.getInstance().getResources(),
                repository
            );
        } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(
                MainApplication.getInstance().getResources(),
                repository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}