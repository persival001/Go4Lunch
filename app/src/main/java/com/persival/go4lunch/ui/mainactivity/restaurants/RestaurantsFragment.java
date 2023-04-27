package com.persival.go4lunch.ui.mainactivity.restaurants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.ui.mainactivity.details.DetailsActivity;

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
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
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

        String location = "48.8566,2.3522"; // Coordonnées de Paris, France
        int radius = 10000; // Rayon de 1000 mètres
        viewModel.fetchRestaurants(location, radius, 10);

        recyclerView.setAdapter(adapter);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantViewStateItems -> {
            adapter.submitList(restaurantViewStateItems);
            Log.d("RECYCLER_VIEW_DATA", "Data submitted to adapter: " + restaurantViewStateItems.toString());
            for (RestaurantsViewState restaurant : restaurantViewStateItems) {
                Log.d("RESTAURANTS_DATA", "Name: " + restaurant.getName() + ", Address: " + restaurant.getTypeOfCuisineAndAddress());
            }
        });

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
