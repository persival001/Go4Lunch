package com.persival.go4lunch.ui.main.maps;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1000;
    private final List<Marker> markers = new ArrayList<>();
    private MapsViewModel mapsViewModel;
    private LatLng lastCameraPosition;
    private float lastZoomLevel;

    @NonNull
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        mapsViewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
            if (!gps) {
                new GpsDialogFragment().show(
                    requireActivity().getSupportFragmentManager(),
                    "GpsDialogFragment"
                );
            } else {
                Log.d("MapsFragment", "GPS ACTIVATED");
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
                } else {
                    Log.d("MapsFragment", "LOCATION IS NULL");
                }
            });

            mapsViewModel.getNearbyRestaurants().observe(getViewLifecycleOwner(), places -> {
                if (places != null) {
                    Log.d("MapsFragment", "PLACES PAS NULL");
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
                        Log.d("MapsFragment", "PLACE: " + place.getName() + " " + place.getAddress());
                        MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        markers.add(googleMap.addMarker(markerOptions));
                    }
                }
            });

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh location permission
        mapsViewModel.onResume();

        // If location permission is not granted, request it
        if (!mapsViewModel.hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsViewModel.stopLocation();
    }

}

