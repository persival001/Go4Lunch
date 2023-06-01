package com.persival.go4lunch.ui.main.maps;

import android.Manifest;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.persival.go4lunch.R;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1000;
    private final List<Marker> markers = new ArrayList<>();
    private MapsViewModel mapsViewModel;
    private GoogleMap googleMap;
    private LatLng lastCameraPosition;
    private float lastZoomLevel;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Display the map on the fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
            .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(map -> {
                googleMap = map;

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

            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        mapsViewModel.getLocationPermission().observe(getViewLifecycleOwner(), hasPermission -> {
            if (hasPermission) {
                mapsViewModel.startLocation();
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity()
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_LOCATION_PERMISSION_CODE
                );
            }
        });

        mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            if (location != null && googleMap != null) {
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

        mapsViewModel.getMarkerOptions().observe(getViewLifecycleOwner(), markerOptionsList -> {
            if (markerOptionsList != null && googleMap != null) {
                // Clear existing markers
                for (Marker marker : markers) {
                    marker.remove();
                }
                markers.clear();

                for (MarkerOptions markerOptions : markerOptionsList) {
                    markers.add(googleMap.addMarker(markerOptions));
                }
            }
        });

        mapsViewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
            if (!gps) {
                new GpsDialogFragment().show(
                    requireActivity().getSupportFragmentManager(),
                    "GpsDialogFragment"
                );
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Refresh location permission
        mapsViewModel.refreshLocationPermission();

        // If location permission is not granted, request it
        if (!mapsViewModel.hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity()
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                , REQUEST_LOCATION_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsViewModel.stopLocation();
    }

}

