package com.persival.go4lunch.ui.main.restaurants;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.ui.main.details.DetailsActivity;

import java.util.List;

public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;
    private RestaurantsViewModel viewModel;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(
            this, ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

        RecyclerView recyclerView = binding.restaurantsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RestaurantsAdapter adapter = new RestaurantsAdapter(
            restaurantId -> startActivity(
                DetailsActivity.navigate(
                    requireContext(),
                    restaurantId
                )));

        recyclerView.setAdapter(adapter);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantsList -> {
            adapter.submitList(restaurantsList);
            updateListVisibility(restaurantsList);
        });
    }

    private void updateListVisibility(List<RestaurantsViewState> list) {
        if (list == null || list.isEmpty()) {
            binding.emptyListImage.setVisibility(View.VISIBLE);
        } else {
            binding.emptyListImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
