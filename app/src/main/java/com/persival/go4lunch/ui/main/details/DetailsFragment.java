package com.persival.go4lunch.ui.main.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.FragmentDetailsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";
    private FragmentDetailsBinding binding;
    private DetailsViewModel viewModel;

    public static DetailsFragment newInstance(@NonNull String restaurantId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_RESTAURANT_ID, restaurantId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        // Set up RecyclerView
        DetailsAdapter detailsAdapter = new DetailsAdapter();
        binding.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.detailsRecyclerView.setAdapter(detailsAdapter);

        // Update UI with restaurant details
        viewModel.getDetailViewStateLiveData().observe(getViewLifecycleOwner(), restaurantDetail -> {
            Glide.with(binding.detailsPicture)
                .load(restaurantDetail.getRestaurantPictureUrl())
                .placeholder(R.drawable.logoresto)
                .error(R.drawable.no_restaurant_picture)
                .centerCrop()
                .into(binding.detailsPicture);
            binding.detailsName.setText(restaurantDetail.getRestaurantName());
            binding.detailsAddress.setText(restaurantDetail.getRestaurantAddress());
            binding.detailsRatingBar.setRating(restaurantDetail.getRestaurantRating());

            // Choose this restaurant for lunch
            binding.chooseThisRestaurantButton.setOnClickListener(view -> viewModel.chooseThisRestaurant(restaurantDetail));
            viewModel.getIsRestaurantChosen().observe(getViewLifecycleOwner(), isChosen -> {
                if (isChosen) {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_ok);

                } else {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_go_fab);
                }
            });

            // Call the restaurant
            binding.callButton.setOnClickListener(view -> {
                if (restaurantDetail.getRestaurantPhoneNumber() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantDetail.getRestaurantPhoneNumber()));
                    startActivity(intent);
                } else {
                    binding.callButton.setVisibility(View.GONE);
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantDetail.getRestaurantPhoneNumber()));
                startActivity(intent);
            });

            // Like this restaurant
            binding.likeButton.setOnClickListener(v -> viewModel.toggleLike());
            if (restaurantDetail.getRestaurantPhoneNumber() != null) {
                binding.callButton.setVisibility(View.VISIBLE);
                viewModel.getIsRestaurantLiked().observe(getViewLifecycleOwner(), isLiked -> {
                    if (isLiked) {
                        binding.likeButton.setIconResource(R.drawable.baseline_star_rate_24);
                    } else {
                        binding.likeButton.setIconResource(R.drawable.baseline_star_border_24);
                    }
                });
            } else {
                binding.callButton.setVisibility(View.GONE);
            }

            // Open the restaurant's website
            binding.websiteButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantDetail.getRestaurantWebsiteUrl()));
                startActivity(intent);
            });

        });

        viewModel.getWorkmateListLiveData().observe(getViewLifecycleOwner(), detailsAdapter::submitList);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide BottomNavigationView and Toolbar when the fragment is visible
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Show BottomNavigationView and Toolbar when the fragment is not visible
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


