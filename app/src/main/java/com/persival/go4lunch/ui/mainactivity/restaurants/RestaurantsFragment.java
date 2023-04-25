package com.persival.go4lunch.ui.mainactivity.restaurants;

import static com.persival.go4lunch.BuildConfig.MAPS_API_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.NearbySearchResponse;
import com.persival.go4lunch.Data.RestClient;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsFragment extends Fragment {

    private RestaurantsViewModel restaurantsViewModel;
    private FragmentRestaurantsBinding binding;

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestaurantsViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        getNearbyRestaurants();
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    private void getNearbyRestaurants() {
        GooglePlacesApi api = RestClient.createGooglePlacesApi();
        String location = "48.8566,2.3522"; // Coordonnées de Paris, France
        int radius = 1000; // Rayon de 1000 mètres
        String type = "restaurant";

        Call<NearbySearchResponse> call = api.getNearbyRestaurants(location, radius, type, MAPS_API_KEY);
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    NearbySearchResponse nearbySearchResponse = response.body();
                    // Utilisez les données de nearbySearchResponse pour afficher les restaurants
                } else {
                    // Gérez les erreurs
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                // Gérez les erreurs
            }
        });
    }
}
