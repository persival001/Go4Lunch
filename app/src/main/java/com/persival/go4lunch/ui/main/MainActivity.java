package com.persival.go4lunch.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.ActivityMainBinding;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsFragment;
import com.persival.go4lunch.ui.main.userlist.UserListFragment;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            switch (item.getItemId()) {
                case R.id.item_1:
                    selectedFragment = RestaurantsFragment.newInstance();
                    break;
                case R.id.item_2:
                    selectedFragment = RestaurantsFragment.newInstance();
                    break;
                case R.id.item_3:
                    selectedFragment = UserListFragment.newInstance();
                    break;
                default:
                    selectedFragment = RestaurantsFragment.newInstance();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            return true;
        });
    }
}