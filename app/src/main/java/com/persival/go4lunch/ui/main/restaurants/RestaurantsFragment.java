package com.persival.go4lunch.ui.main.restaurants;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;
import com.persival.go4lunch.ui.main.details.DetailsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantsFragment extends Fragment {
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1000;
    private FragmentRestaurantsBinding binding;
    private RestaurantsViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        viewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
            if (!gps) {
                new GpsDialogFragment().show(
                    requireActivity().getSupportFragmentManager(),
                    "GpsDialogFragment"
                );
            }
        });

        viewModel.getLocationPermission().observe(getViewLifecycleOwner(), hasPermission -> {
            if (hasPermission) {
                viewModel.startLocation();
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity()
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_LOCATION_PERMISSION_CODE
                );
            }
        });

        // Bind the RecyclerView and set the adapter
        RecyclerView recyclerView = binding.restaurantsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        RestaurantsAdapter adapter = new RestaurantsAdapter(restaurantId -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(restaurantId);
            getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, detailsFragment)
                .addToBackStack(null)
                .commit();
        });
        recyclerView.setAdapter(adapter);

        viewModel.getSortedRestaurantsLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Refresh location permission
        viewModel.refreshLocationPermission();

        // If location permission is not granted, request it
        if (!viewModel.hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity()
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , REQUEST_LOCATION_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stopLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
