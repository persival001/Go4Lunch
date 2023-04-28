package com.persival.go4lunch.ui.mainactivity.restaurants;

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

        RestaurantsAdapter adapter = new RestaurantsAdapter(restaurantId -> startActivity(DetailsActivity.navigate(requireContext(), restaurantId)));

        String location = "48.6921,6.1844"; // Nancy
        int radius = 500; // Rayon de 500 mètres
        viewModel.fetchRestaurants(location, radius);

        recyclerView.setAdapter(adapter);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
