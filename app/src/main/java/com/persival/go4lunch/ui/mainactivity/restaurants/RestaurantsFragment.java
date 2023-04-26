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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.Data.GooglePlacesApi;
import com.persival.go4lunch.Data.NearbySearchResponse;
import com.persival.go4lunch.Data.RestClient;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.ui.mainactivity.details.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsFragment extends Fragment {

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        //getNearbyRestaurants();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RestaurantsViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.restaurants_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RestaurantsAdapter adapter = new RestaurantsAdapter(new OnRestaurantClickedListener() {
            @Override
            public void onRestaurantClicked(long restaurantId) {
                startActivity(DetailsActivity.navigate(requireContext(), restaurantId));
            }
        });

        recyclerView.setAdapter(adapter);



        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantViewStateItems ->
            adapter.submitList(restaurantViewStateItems)
        );

        /*List<RestaurantsViewState> restaurants = new ArrayList<>();

        // Ajoutez des restaurants fictifs à la liste
        restaurants.add(new RestaurantsViewState(1, "Restaurant 1", "Italian - 42 Rue de la Mifa", "Open until 23pm", "210m", "(1)", "https://picsum.photos/200/300"));
        restaurants.add(new RestaurantsViewState(2, "Restaurant 2", "French - 12 Rue de la Mifa", "Open until 22pm", "350m", "(3)", "https://picsum.photos/200/300"));
        restaurants.add(new RestaurantsViewState(3, "Restaurant 3", "Mexican - 25 Rue de la Mifa", "Open until 24pm", "500m", "(2)", "https://picsum.photos/200/300"));
        restaurants.add(new RestaurantsViewState(4, "Restaurant 4", "Japanese - 15 Rue de la Mifa", "Open until 21pm", "700m", "(4)", "https://picsum.photos/200/300"));
        // Ajoutez plus de restaurants fictifs si nécessaire

        // Mettez à jour l'adaptateur avec la liste des restaurants
        adapter.submitList(restaurants);*/
    }
}
