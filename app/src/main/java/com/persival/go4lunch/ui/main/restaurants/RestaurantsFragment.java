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
import com.persival.go4lunch.ui.main.details.DetailsFragment;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

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
    public void onStop() {
        super.onStop();
        viewModel.stopLocationRequest();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
