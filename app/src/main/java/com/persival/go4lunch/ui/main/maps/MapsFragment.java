package com.persival.go4lunch.ui.main.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.persival.go4lunch.data.places.model.NearbyRestaurantsResponse;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends SupportMapFragment {
    private final List<Marker> markers = new ArrayList<>();
    private MapsViewModel mapsViewModel;
    private LatLng lastCameraPosition;
    private float lastZoomLevel;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @NonNull
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        // Initialize requestPermissionLauncher
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                mapsViewModel.onResume();
            }
        });

        getMapAsync(googleMap -> {
            googleMap.setOnMarkerClickListener(marker -> {
                lastCameraPosition = googleMap.getCameraPosition().target;
                lastZoomLevel = googleMap.getCameraPosition().zoom;

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18));
                marker.showInfoWindow();

                return true;
            });

            googleMap.setOnMapClickListener(latLng -> {
                if (lastCameraPosition != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastCameraPosition, lastZoomLevel));
                }
            });

            mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Move the camera to the current location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                    // Add a circle around the current location
                    CircleOptions circleOptions = new CircleOptions()
                        .center(latLng)
                        .radius(5000)
                        .strokeColor(Color.BLUE)
                        .fillColor(0x110000FF);
                    googleMap.addCircle(circleOptions);
                }
            });

            mapsViewModel.getNearbyRestaurants().observe(getViewLifecycleOwner(), places -> {
                if (places != null) {
                    // Clear existing markers
                    for (Marker marker : markers) {
                        marker.remove();
                    }
                    markers.clear();

                    // Create new markers
                    for (NearbyRestaurantsResponse.Place place : places) {
                        LatLng latLng = new LatLng(
                            place.getLatitude(),
                            place.getLongitude()
                        );
                        MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        markers.add(googleMap.addMarker(markerOptions));
                    }
                }
            });

            // Check GPS activation
            mapsViewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
                if (!gps) {
                    Log.d("MapsFragment", "GPS DEACTIVATED");
                    new GpsDialogFragment().show(
                        requireActivity().getSupportFragmentManager(),
                        "GpsDialogFragment"
                    );
                } else {
                    Log.d("MapsFragment", "GPS ACTIVATED");
                }
            });

        });
    }

    @Override
    public void onResume() {
        mapsViewModel.refreshGpsActivation();
        mapsViewModel.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsViewModel.stopLocation();
    }

}

