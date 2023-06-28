package com.persival.go4lunch.ui.main.restaurants;

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
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;
import com.persival.go4lunch.ui.main.details.DetailsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;

    @NonNull
    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String searchString = getArguments().getString("searchString");
            RestaurantsViewModel viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);
            viewModel.updateSearchString(searchString);
        }
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

        RestaurantsViewModel viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        // Observe the GPS activation
        viewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
            if (Boolean.FALSE.equals(gps)) {
                new GpsDialogFragment().show(
                    requireActivity().getSupportFragmentManager(),
                    "GpsDialogFragment"
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

        // Observe and submit the list of restaurants
        viewModel.getSortedRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            adapter.submitList(restaurants);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
