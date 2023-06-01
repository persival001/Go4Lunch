package com.persival.go4lunch.ui.main.maps;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
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
    private LatLng lastCameraPosition;
    private float lastZoomLevel;

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
                // Do something when permission is not granted
            }
        });

        // Observe the location live data for refreshing the map
        mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            if (location != null && googleMap != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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

                // Add a circle around the user location
                mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        CircleOptions circleOptions = new CircleOptions()
                            .center(userLocation)
                            .radius(5000)
                            .strokeColor(Color.BLUE)
                            .fillColor(0x110000FF);

                        googleMap.addCircle(circleOptions);
                    }
                });

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
            mapsViewModel.requestLocationPermission(requireActivity(), REQUEST_LOCATION_PERMISSION_CODE);
        }
    }

}

