package com.persival.go4lunch.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;
import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.ActivityMainBinding;
import com.persival.go4lunch.databinding.NavigationDrawerHeaderBinding;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.ui.authentication.AuthenticationActivity;
import com.persival.go4lunch.ui.main.details.DetailsFragment;
import com.persival.go4lunch.ui.main.maps.MapsFragment;
import com.persival.go4lunch.ui.main.restaurants.RestaurantsFragment;
import com.persival.go4lunch.ui.main.settings.SettingsFragment;
import com.persival.go4lunch.ui.main.workmates.WorkmatesFragment;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private String restaurantId;
    private ListenerRegistration listenerRegistration;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_your_lunch) {
            if (restaurantId == null) {
                Toast.makeText(this, R.string.no_restaurant_selected, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(restaurantId);
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, detailsFragment)
                    .addToBackStack(null)
                    .commit();
            }

        } else if (itemId == R.id.nav_settings) {
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, settingsFragment)
                .addToBackStack(null)
                .commit();

        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set up the bottom navigation bar and handle item selection
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = getSelectedFragment(item.getItemId());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            return true;
        });

        // Set the toolbar and disable its title
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Set up the action bar drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation drawer
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        // Inflate the header view of the navigation drawer at runtime
        View headerView = navigationView.getHeaderView(0);
        NavigationDrawerHeaderBinding navHeaderBinding = NavigationDrawerHeaderBinding.bind(headerView);

        // Observe the user data from the ViewModel and display it in the navigation drawer header
        viewModel.getAuthenticatedUserLiveData().observe(MainActivity.this, user -> {
            navHeaderBinding.userName.setText(user.getAvatarName());
            navHeaderBinding.userEmail.setText(user.getEmail());
            Glide.with(navHeaderBinding.userImage)
                .load(user.getAvatarPictureUrl())
                .placeholder(R.drawable.ic_anon_user_48dp)
                .error(R.drawable.ic_anon_user_48dp)
                .circleCrop()
                .into(navHeaderBinding.userImage);
        });

        viewModel.getRestaurantIdForCurrentUserLiveData().observe(
            MainActivity.this,
            restaurantIdForCurrentUser -> restaurantId = restaurantIdForCurrentUser
        );

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_view);
        ArrayAdapter<NearbyRestaurantsEntity> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            new ArrayList<>()
        );

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);

        viewModel.getFilteredRestaurantsLiveData().observe(MainActivity.this, restaurants -> {
            adapter.clear();
            adapter.addAll(restaurants);
            adapter.notifyDataSetChanged();
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            NearbyRestaurantsEntity selectedRestaurant = (NearbyRestaurantsEntity) parent.getItemAtPosition(position);
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

            if (currentFragment instanceof MapsFragment) {
                // Use the selectedRestaurant restaurantId to launch DetailsFragment
                Toast.makeText(this, selectedRestaurant.getName(), Toast.LENGTH_SHORT).show();
                /*MapsFragment mapsFragment = MapsFragment.newInstance(selectedRestaurant.getId());
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, mapsFragment)
                    .addToBackStack(null)
                    .commit();*/
            } else if (currentFragment instanceof RestaurantsFragment) {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(selectedRestaurant.getId());
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, detailsFragment)
                    .addToBackStack(null)
                    .commit();
            }

            // Close the soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            }

            // Clear the search field
            autoCompleteTextView.setText("");
        });


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    viewModel.updateSearchString(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // If there's no saved instance state, load the MapsFragment into the fragment container view
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MapsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_your_lunch);
        }
    }

    private Fragment getSelectedFragment(int itemId) {
        if (itemId == R.id.item_2) {
            return RestaurantsFragment.newInstance();
        } else if (itemId == R.id.item_3) {
            return WorkmatesFragment.newInstance();
        } else {
            return MapsFragment.newInstance();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

}