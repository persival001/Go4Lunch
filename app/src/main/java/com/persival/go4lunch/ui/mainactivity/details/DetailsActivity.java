package com.persival.go4lunch.ui.mainactivity.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ViewModelFactory;
import com.persival.go4lunch.databinding.ActivityDetailsBinding;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";
    private final DetailsAdapter detailsAdapter = new DetailsAdapter();
    private ActivityDetailsBinding binding;

    public static Intent navigate(Context context, String restaurantId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String restaurantId = getIntent().getStringExtra(KEY_RESTAURANT_ID);

        if (Objects.equals(restaurantId, "")) {
            throw new IllegalStateException("Please use DetailsActivity.navigate() to launch the Activity");
        }

        DetailsViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewModel.class);

        viewModel.getDetailViewStateLiveData(restaurantId).observe(this, restaurantDetailViewState -> {
            Glide.with(binding.detailsPicture)
                .load(restaurantDetailViewState.getPictureUrl())
                .placeholder(R.drawable.logoresto)
                .error(R.drawable.baseline_error_24)
                .into(binding.detailsPicture);
            binding.detailsName.setText(restaurantDetailViewState.getName());
            binding.detailsAddress.setText(restaurantDetailViewState.getTypeOfCuisineAndAddress());
            binding.detailsRatingBar.setRating(restaurantDetailViewState.getRating());
        });

        binding.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailsRecyclerView.setAdapter(detailsAdapter);

        //viewModel.getDetailRestaurantLiveData().observe(this, detailsAdapter::submitList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

