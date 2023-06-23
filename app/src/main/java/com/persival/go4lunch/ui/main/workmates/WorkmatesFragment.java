package com.persival.go4lunch.ui.main.workmates;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.FragmentUserListBinding;
import com.persival.go4lunch.ui.main.details.DetailsFragment;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmatesFragment extends Fragment {

    private FragmentUserListBinding binding;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentUserListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WorkmatesViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        RecyclerView recyclerView = binding.userListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        WorkmatesAdapter adapter = new WorkmatesAdapter(restaurantId -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(restaurantId);
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, detailsFragment)
                .addToBackStack(null)
                .commit();
        });


        recyclerView.setAdapter(adapter);

        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(
            getViewLifecycleOwner(),
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, RestaurantsFragment.newInstance())
                        .commit();
                }

            }
        );
    }
}
