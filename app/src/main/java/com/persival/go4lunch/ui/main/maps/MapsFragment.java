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
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                }
            }
        });


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
}

