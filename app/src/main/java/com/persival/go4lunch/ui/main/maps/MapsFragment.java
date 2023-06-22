package com.persival.go4lunch.ui.main.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.persival.go4lunch.R;
import com.persival.go4lunch.domain.restaurant.model.NearbyRestaurantsEntity;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;
import com.persival.go4lunch.ui.main.details.DetailsFragment;
import com.persival.go4lunch.ui.main.settings.SettingsFragment;

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
    private GoogleMap googleMap;

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
            this.googleMap = googleMap;
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

                    // Ajoutez un marqueur à la position de l'utilisateur
                    MarkerOptions userMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.your_position))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    googleMap.addMarker(userMarkerOptions);

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
                    for (NearbyRestaurantsEntity place : places) {
                        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        Marker marker = googleMap.addMarker(markerOptions);
                        if (marker != null) {
                            marker.setTag(place.getId());
                        }
                        markers.add(marker);
                    }

                }
            });

            // Navigate to DetailsFragment when clicking on a marker
            googleMap.setOnInfoWindowClickListener(marker -> {
                String restaurantId = (String) marker.getTag();
                DetailsFragment detailsFragment = null;
                if (restaurantId != null) {
                    detailsFragment = DetailsFragment.newInstance(restaurantId);
                }

                if (detailsFragment != null) {
                    getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, detailsFragment)
                        .addToBackStack(null)
                        .commit();
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

    public void zoomToMarker(NearbyRestaurantsEntity selectedRestaurant) {
        if (selectedRestaurant != null && googleMap != null) {
            LatLng latLng = new LatLng(selectedRestaurant.getLatitude(), selectedRestaurant.getLongitude());

            lastCameraPosition = googleMap.getCameraPosition().target;
            lastZoomLevel = googleMap.getCameraPosition().zoom;

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
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
        requireActivity().getOnBackPressedDispatcher().addCallback(
            getViewLifecycleOwner(),
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, SettingsFragment.newInstance())
                        .commit();
                }

            }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsViewModel.stopLocation();
    }

}

