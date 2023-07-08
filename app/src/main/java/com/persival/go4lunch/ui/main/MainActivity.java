package com.persival.go4lunch.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.persival.go4lunch.R;
import com.persival.go4lunch.databinding.ActivityMainBinding;
import com.persival.go4lunch.databinding.NavigationDrawerHeaderBinding;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.ui.authentication.AuthenticationActivity;
import com.persival.go4lunch.ui.details.DetailsFragment;
import com.persival.go4lunch.ui.maps.MapsFragment;
import com.persival.go4lunch.ui.restaurants.RestaurantsFragment;
import com.persival.go4lunch.ui.settings.SettingsFragment;
import com.persival.go4lunch.ui.workmates.WorkmatesFragment;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private String searchString = "";
    private String restaurantId;

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
            new Handler().postDelayed(this::invalidateOptionsMenu, 100);
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
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, com.google.android.libraries.places.R.color.quantum_white_100));

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

        // Observe the restaurantId of the restaurant the user has chosen for lunch
        viewModel.getRestaurantIdForCurrentUserLiveData().observe(
            MainActivity.this,
            restaurantIdForCurrentUser -> restaurantId = restaurantIdForCurrentUser
        );

        // Execute the research in the opened fragment
        viewModel.getMatchedRestaurants().observe(this, item -> {
            if (item != null) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (currentFragment instanceof MapsFragment) {
                    ((MapsFragment) currentFragment).displayRestaurantsMarkers(item);
                }

                if (currentFragment instanceof RestaurantsFragment && !searchString.isEmpty()) {
                    RestaurantsFragment restaurantsFragment = RestaurantsFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("searchString", searchString);
                    restaurantsFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, restaurantsFragment)
                        .addToBackStack(null)
                        .commit();
                }
            }
        });

        // Close the search view when the user clicks on the done button
        binding.searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                setSearchViewGone();
                return true;
            }
            return false;
        });

        // Start searching when the user types 2 characters
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchString = s.toString();
                viewModel.updateSearchString(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Observe changes on the live data returned by getMatchedRestaurants() and update the adapter.
        viewModel.getMatchedRestaurants().observe(this, restaurants -> {
            ArrayAdapter<NearbyRestaurantsEntity> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, restaurants);
            binding.searchView.setAdapter(adapter);
        });

        // Close drawer when back button is pressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    this.setEnabled(false);
                    onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        searchItem.setVisible(!(currentFragment instanceof WorkmatesFragment));
        return super.onPrepareOptionsMenu(menu);
    }

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
                new Handler().postDelayed(this::invalidateOptionsMenu, 100);
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
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            binding.textView.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.VISIBLE);
            binding.searchView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSearchViewGone() {
        //Close keyboard and search view
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.searchView.getWindowToken(), 0);
        }

        binding.searchView.setText("");
        binding.textView.setVisibility(View.VISIBLE);
        binding.searchView.setVisibility(View.GONE);
    }

}