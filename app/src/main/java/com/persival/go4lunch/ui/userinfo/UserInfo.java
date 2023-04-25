package com.persival.go4lunch.ui.userinfo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.persival.go4lunch.databinding.ActivityMainBinding;
import com.persival.go4lunch.databinding.ActivityUserInfoBinding;

public class UserInfo extends AppCompatActivity {

    private ActivityUserInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}