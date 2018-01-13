package com.example.snehi.knowyourgov;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class Locator {

    private static final String TAG = "Locator";
    private MainActivity owner;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locator(MainActivity activity) {
        owner = activity;
        setUpLocationManager();
    }

    public void setUpLocationManager() {

        if (locationManager != null)
            return;

        if (!checkPermission())
            return;

        locationManager = (LocationManager) owner.getSystemService(LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                owner.setData(location.getLatitude(), location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

		//Register the listener with the Location Manager to receive location updates

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        // shutDown();

    }

    public void shutDown() {
        if(locationManager!=null)
            locationManager.removeUpdates(locationListener);
            locationManager = null;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(owner,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

    public boolean determineLocationNetwork() {
        if (checkPermission() == true) {
            if (locationManager != null)
                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER ))
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER ))
                    {
                    Toast.makeText(owner, "Network Location provider is not available!", Toast.LENGTH_LONG).show();
                    }
                if (locationManager != null) {

                    Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location loc1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d(TAG, "determineLocationNetwork: location"+loc);
                    Log.d(TAG, "determineLocationNetwork: location"+loc1);
                    if (loc != null) {
                        owner.setData(loc.getLatitude(), loc.getLongitude());
                        return true;
                    }
                    else if(loc1!=null) {
                        owner.setData(loc1.getLatitude(), loc1.getLongitude());
                        return true;
                    }
                    else {
                        Toast.makeText(owner, "Please check location services are switched off!", Toast.LENGTH_SHORT).show();
                    }

                }
        }
        return false;
    }
}

