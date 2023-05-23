package com.persival.go4lunch.ui.main.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.persival.go4lunch.R;
import com.persival.go4lunch.data.model.NearbyRestaurantsResponse;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1000;
    private MapsViewModel mapsViewModel;
    private GoogleMap googleMap;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Display the map on the fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
            .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(map -> {
                googleMap = map;
            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        // Observe the location live data for refreshing the map
        mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            if (location != null && googleMap != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        mapsViewModel.getNearbyRestaurants().observe(getViewLifecycleOwner(), places -> {
            if (places != null && googleMap != null) {

                // Clear existing markers
                googleMap.clear();

                for (NearbyRestaurantsResponse.Place place : places) {
                    LatLng latLng = new LatLng(
                        place.getLatitude(),
                        place.getLongitude()
                    );
                    googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(place.getName())
                        .snippet(place.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    );
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (mapsViewModel.hasLocationPermission()) {
            mapsViewModel.startLocationRequest();
        } else {
            ActivityCompat.requestPermissions(requireActivity()
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , REQUEST_LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapsViewModel.refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsViewModel.stopLocationRequest();
    }

    /*public void searchRestaurant() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String location = s.toString();
                if (location.length() < 3) {
                    // If less than 3 characters have been entered, do not perform search
                    return;
                }
                // Use a Geocoder to get the latitude and longitude from the location string
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(location, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        // Create a LatLng object from the Address
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        // Move the camera to the position
                        if (googleMap != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }*/
}

