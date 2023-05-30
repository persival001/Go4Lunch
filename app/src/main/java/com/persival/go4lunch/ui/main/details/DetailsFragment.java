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

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    private static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";
    private String restaurantId;
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
        if (getArguments() != null) {
            restaurantId = getArguments().getString(KEY_RESTAURANT_ID);
        }
        if (Objects.equals(restaurantId, "")) {
            throw new IllegalStateException("Please use DetailsFragment.newInstance() to launch the Fragment");
        }
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        DetailsAdapter detailsAdapter = new DetailsAdapter();
        binding.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.detailsRecyclerView.setAdapter(detailsAdapter);

        viewModel.getDetailViewStateLiveData(restaurantId).observe(getViewLifecycleOwner(), restaurantDetail -> {
            Glide.with(binding.detailsPicture)
                .load(restaurantDetail.getPictureUrl())
                .placeholder(R.drawable.logoresto)
                .error(R.drawable.no_restaurant_picture)
                .centerCrop()
                .into(binding.detailsPicture);
            binding.detailsName.setText(restaurantDetail.getName());
            binding.detailsAddress.setText(restaurantDetail.getAddress());
            binding.detailsRatingBar.setRating(restaurantDetail.getRating());
            binding.websiteButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantDetail.getWebsite()));
                startActivity(intent);
            });

            binding.callButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantDetail.getPhoneNumber()));
                startActivity(intent);
            });

            binding.likeButton.setOnClickListener(v -> viewModel.toggleLike());

            binding.chooseThisRestaurantButton.setOnClickListener(view -> viewModel.chooseThisRestaurant(restaurantDetail));

            viewModel.getIsRestaurantLiked().observe(getViewLifecycleOwner(), isLiked -> {
                if (isLiked) {
                    binding.likeButton.setIcon(getResources().getDrawable(R.drawable.baseline_star_rate_24));
                } else {
                    binding.likeButton.setIcon(getResources().getDrawable(R.drawable.baseline_star_border_24));
                }
            });

            viewModel.getIsRestaurantChosen().observe(getViewLifecycleOwner(), isChosen -> {
                if (isChosen) {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_ok);
                } else {
                    binding.chooseThisRestaurantButton.setImageResource(R.drawable.ic_go_fab);
                }
            });

            binding.chooseThisRestaurantButton.setOnClickListener(v -> viewModel.chooseThisRestaurant(restaurantDetail));


        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), detailsAdapter::submitList);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.GONE);
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.VISIBLE);
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


