package com.persival.go4lunch.ui.main.restaurants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.ui.main.details.DetailsActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantsFragment extends Fragment {

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
            );
        }

        RecyclerView recyclerView = binding.restaurantsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        RestaurantsAdapter adapter = new RestaurantsAdapter(
            restaurantId -> startActivity(
                DetailsActivity.navigate(
                    requireContext(),
                    restaurantId
                )
            )
        );
        recyclerView.setAdapter(adapter);

        viewModel.getSortedRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantsList -> {
            adapter.submitList(restaurantsList);
        });
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
