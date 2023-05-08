package com.persival.go4lunch.ui.main.restaurants;

import static com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel.ANIMATION_STATUS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(
            this, ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

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

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurantsList -> {
            adapter.submitList(restaurantsList);
            updateListVisibility(restaurantsList);
        });

        viewModel.getGpsMessageLiveData().observe(getViewLifecycleOwner(), status -> {
            binding.gpsMessageProgressBar.setVisibility(View.GONE);
            binding.gpsMessageImage.setVisibility(View.GONE);
            switch (status) {
                case ANIMATION_STATUS:
                    binding.gpsMessageProgressBar.setVisibility(View.VISIBLE);
                    break;
                case R.drawable.baseline_location_off_24:
                    binding.gpsMessageImage.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
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

    private void updateListVisibility(List<RestaurantsViewState> list) {
        if (list == null || list.isEmpty()) {
            binding.gpsMessageProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.gpsMessageProgressBar.setVisibility(View.GONE);
        }
    }
}
