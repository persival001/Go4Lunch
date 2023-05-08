package com.persival.go4lunch.ui.user_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.persival.go4lunch.databinding.ActivityUserInfoBinding;
import com.persival.go4lunch.ui.main.details.DetailsActivity;
import com.persival.go4lunch.ui.main.maps.MapsFragment;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;

    public static Intent navigate(Context context) {
        return new Intent(context, DetailsActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}