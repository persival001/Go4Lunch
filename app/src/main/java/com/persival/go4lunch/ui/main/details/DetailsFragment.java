package com.persival.go4lunch.ui.main.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.FragmentDetailsBinding;
import com.persival.go4lunch.ui.notifications.RestaurantReminderWorker;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        // Listener for the -back- button
        binding.backButton.setOnClickListener(v -> requireActivity().onBackPressed());

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
            viewModel.getIsRestaurantChosenLiveData().observe(getViewLifecycleOwner(), isChosen -> {
                // Update the button state based on the isChosen value.
                binding.chooseThisRestaurantButton.setSelected(isChosen);

                // Change the image of the button based on whether it's chosen or not
                if (Boolean.TRUE.equals(isChosen)) {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_ok);

                    // Put information's for the notification worker
                    viewModel.getNotificationLiveData().observe(getViewLifecycleOwner(), notification -> {
                        if (notification != null) {
                            Data myData = new Data.Builder()
                                .putString("restaurantName", notification.getRestaurantName())
                                .putString("restaurantAddress", notification.getRestaurantAddress())
                                .putString("workmates", notification.getWorkmatesNames())
                                .build();

                            // Creation of notification
                            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RestaurantReminderWorker.class)
                                .setInputData(myData)
                                .setInitialDelay(timeUntilNoon(), TimeUnit.MILLISECONDS)
                                .build();

                            // Get the UUID of the WorkRequest
                            UUID workId = workRequest.getId();
                            // Enqueue the work
                            WorkManager.getInstance(requireContext()).enqueue(workRequest);
                            // Save the UUID for future reference
                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("workId", workId.toString());
                            editor.apply();
                        }
                    });

                } else {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_go_fab);

                    // Cancel the work if the restaurant is deselected
                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    String workIdString = sharedPref.getString("workId", null);
                    if (workIdString != null) {
                        UUID workId = UUID.fromString(workIdString);
                        WorkManager.getInstance(requireContext()).cancelWorkById(workId);
                    }
                }
            });

            binding.chooseThisRestaurantButton.setOnClickListener(view -> viewModel.onChooseRestaurant(restaurantDetail));

            // Call the restaurant
            binding.callButton.setOnClickListener(view -> {
                if (restaurantDetail.getRestaurantPhoneNumber() != null) {
                    binding.callButton.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantDetail.getRestaurantPhoneNumber()));
                    startActivity(intent);
                } else {
                    binding.callButton.setVisibility(View.GONE);
                }
            });

            // Like this restaurant
            viewModel.likedRestaurantsLiveData.observe(getViewLifecycleOwner(), likedRestaurants -> {
                if (likedRestaurants != null) {
                    viewModel.updateIsRestaurantLiked(likedRestaurants.contains(restaurantDetail.getId()));
                }
            });
            viewModel.getIsRestaurantLiked().observe(getViewLifecycleOwner(), isLiked -> {
                if (Boolean.TRUE.equals(isLiked)) {
                    binding.likeButton.setIconResource(R.drawable.baseline_star_rate_24);
                } else {
                    binding.likeButton.setIconResource(R.drawable.baseline_star_border_24);
                }
            });
            binding.likeButton.setOnClickListener(v -> viewModel.onToggleLikeRestaurant());

            // Open the restaurant's website
            binding.websiteButton.setOnClickListener(view -> {
                if (restaurantDetail.getRestaurantWebsiteUrl() != null) {
                    binding.websiteButton.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantDetail.getRestaurantWebsiteUrl()));
                    startActivity(intent);
                } else {
                    binding.websiteButton.setVisibility(View.GONE);
                }
            });

        });

        viewModel.getWorkmateListLiveData().observe(getViewLifecycleOwner(), detailsAdapter::submitList);

        return binding.getRoot();
    }

    public long timeUntilNoon() {
        Calendar now = Calendar.getInstance();
        Calendar noon = Calendar.getInstance();
        noon.set(Calendar.HOUR_OF_DAY, 12);
        noon.set(Calendar.MINUTE, 0);

        return noon.getTimeInMillis() - now.getTimeInMillis();
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


