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

public class DetailsActivity extends AppCompatActivity {

    private static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";
    private final DetailsAdapter detailsAdapter = new DetailsAdapter();
    private ActivityDetailsBinding binding;

    public static Intent navigate(Context context, long restaurantId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long restaurantId = getIntent().getLongExtra(KEY_RESTAURANT_ID, -1);

        if (restaurantId == -1) {
            throw new IllegalStateException("Please use DetailsActivity.navigate() to launch the Activity");
        }

        DetailsViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewModel.class);

        viewModel.getRestaurantDetailsViewStateLiveData(restaurantId);

        binding.detailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.detailsRecyclerView.setAdapter(detailsAdapter);

        viewModel.getDetailRestaurantLiveData().observe(this, detailsAdapter::submitList);
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

