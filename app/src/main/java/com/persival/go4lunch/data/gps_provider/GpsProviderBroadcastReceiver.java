package com.persival.go4lunch.data.gps_provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.persival.go4lunch.data.PermissionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GpsProviderBroadcastReceiver extends BroadcastReceiver {

    private PermissionRepository permissionRepository;

    @Inject
    public GpsProviderBroadcastReceiver(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            // Le statut du GPS a changé

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Le GPS a été activé
                permissionRepository.refreshLocationPermission();
            } else {
                // Le GPS a été désactivé
                // Faire quelque chose ici, si nécessaire
            }
        }
    }

}
