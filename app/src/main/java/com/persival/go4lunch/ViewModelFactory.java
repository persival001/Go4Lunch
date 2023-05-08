package com.persival.go4lunch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.persival.go4lunch.data.permission_checker.PermissionChecker;
import com.persival.go4lunch.data.repository.GooglePlacesRepository;
import com.persival.go4lunch.data.repository.LocationRepository;
import com.persival.go4lunch.ui.main.details.DetailsViewModel;
import com.persival.go4lunch.ui.main.maps.MapsViewModel;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel;
import com.persival.go4lunch.ui.main.user_list.UserListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private volatile static ViewModelFactory sInstance;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final GooglePlacesRepository googlePlacesRepository;

    private ViewModelFactory(
        @NonNull PermissionChecker permissionChecker,
        @NonNull LocationRepository locationRepository,
        @NonNull GooglePlacesRepository googlePlacesRepository
    ) {
        this.googlePlacesRepository = googlePlacesRepository;
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
    }

    public static ViewModelFactory getInstance() {
        if (sInstance == null) {
            synchronized (ViewModelFactory.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new ViewModelFactory(
                        new PermissionChecker(
                            application
                        ),
                        new LocationRepository(
                            LocationServices.getFusedLocationProviderClient(
                                application
                            ), application
                        ),
                        new GooglePlacesRepository()
                    );
                }
            }
        }

        return sInstance;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(
                locationRepository,
                permissionChecker
            );
        } else if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel(
                googlePlacesRepository,
                permissionChecker,
                locationRepository
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