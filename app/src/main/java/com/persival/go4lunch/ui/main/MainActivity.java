package com.persival.go4lunch.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.ActivityMainBinding;
import com.persival.go4lunch.ui.main.maps.MapsFragment;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsFragment;
import com.persival.go4lunch.ui.main.userlist.UserListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            switch (item.getItemId()) {
                case R.id.item_1:
                    selectedFragment = MapsFragment.newInstance();
                    break;
                case R.id.item_2:
                    selectedFragment = RestaurantsFragment.newInstance();
                    break;
                case R.id.item_3:
                    selectedFragment = UserListFragment.newInstance();
                    break;
                default:
                    selectedFragment = MapsFragment.newInstance();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            return true;
        });
    }
}