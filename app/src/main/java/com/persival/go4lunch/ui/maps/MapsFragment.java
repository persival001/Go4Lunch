package com.persival.go4lunch.ui.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.persival.go4lunch.ui.details.DetailsFragment;
import com.persival.go4lunch.ui.gps_dialog.GpsDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        // Check GPS activation
        mapsViewModel.isGpsActivatedLiveData().observe(getViewLifecycleOwner(), gps -> {
            if (Boolean.FALSE.equals(gps)) {
                new GpsDialogFragment().show(
                    requireActivity().getSupportFragmentManager(),
                    "GpsDialogFragment"
                );
            }
        });

        // Initialize requestPermissionLauncher
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (Boolean.TRUE.equals(isGranted)) {
                mapsViewModel.onResume();
            }
        });

        // Markers listener for zoom and camera position
        getMapAsync(googleMapLocal -> {
            this.googleMap = googleMapLocal;
            googleMap.setOnMarkerClickListener(marker -> {
                lastCameraPosition = googleMap.getCameraPosition().target;
                lastZoomLevel = googleMap.getCameraPosition().zoom;

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18));
                marker.showInfoWindow();

                return true;
            });

            // Map listener for get the last zoom and camera position
            googleMap.setOnMapClickListener(latLng -> {
                if (lastCameraPosition != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastCameraPosition, lastZoomLevel));
                }
            });

            // Add marker for user position
            mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

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

            // Add markers for nearby restaurants
            mapsViewModel.getRestaurantsWithParticipants().observe(getViewLifecycleOwner(), data -> {
                if (data != null) {
                    // Clear existing markers
                    for (Marker marker : markers) {
                        marker.remove();
                    }
                    markers.clear();

                    // Create new markers
                    for (Map.Entry<NearbyRestaurantsEntity, Integer> entry : data.entrySet()) {
                        NearbyRestaurantsEntity place = entry.getKey();
                        Integer participants = entry.getValue();

                        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getAddress());

                        if (participants != null && participants > 0) {
                            // If the restaurant is occupied, set the marker color to green
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        } else {
                            // If the restaurant is not occupied, set the marker color to orange
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        }

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

        });
    }

    public void zoomToMarker(NearbyRestaurantsEntity selectedRestaurant) {
        if (selectedRestaurant != null && googleMap != null) {
            LatLng latLng = new LatLng(selectedRestaurant.getLatitude(), selectedRestaurant.getLongitude());

            lastCameraPosition = googleMap.getCameraPosition().target;
            lastZoomLevel = googleMap.getCameraPosition().zoom;

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        } else {
            Toast.makeText(requireContext(), getString(R.string.no_restaurant_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        // Refresh GPS activation and location permission
        mapsViewModel.refreshGpsActivation();
        mapsViewModel.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        super.onResume();

        // Double back press to exit
        final boolean[] wasBackPressed = {false};

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        requireActivity().getOnBackPressedDispatcher().addCallback(
            getViewLifecycleOwner(),
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (wasBackPressed[0]) {
                        this.setEnabled(false);
                        requireActivity().finishAffinity();
                    } else {
                        wasBackPressed[0] = true;
                        Toast.makeText(requireContext(), getString(R.string.exit), Toast.LENGTH_SHORT).show();
                        executorService.schedule(() -> wasBackPressed[0] = false, 2, TimeUnit.SECONDS);
                    }
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

