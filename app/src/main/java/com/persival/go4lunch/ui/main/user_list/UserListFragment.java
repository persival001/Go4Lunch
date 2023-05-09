package com.persival.go4lunch.ui.main.user_list;

import static com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel.ANIMATION_STATUS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.databinding.FragmentRestaurantsBinding;
import com.persival.go4lunch.databinding.FragmentUserListBinding;
import com.persival.go4lunch.ui.main.details.DetailsActivity;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsAdapter;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsFragment;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewModel;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsViewState;

import java.util.List;

public class UserListFragment extends Fragment {

    private FragmentUserListBinding binding;
    private UserListViewModel viewModel;

    public static UserListFragment newInstance() {
        return new UserListFragment();
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
        viewModel = new ViewModelProvider(
            this, ViewModelFactory.getInstance()).get(UserListViewModel.class);

        RecyclerView recyclerView = binding.userListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        UserListAdapter adapter = new UserListAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.populateUserListLiveData().observe(getViewLifecycleOwner(), userList -> {
            adapter.submitList(userList);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}