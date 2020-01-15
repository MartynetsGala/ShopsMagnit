package ru.example.shopsmagnit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Date;

public class ShopListActivity extends SingleFragmentActivity {

    private LocationManager locationManager;
    private static final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 10;
    public Fragment mFragment;

    public double mLat = 39.002729;
    public double mLng = 45.074233;

    SharedPreferences sPref;
    final String SAVED_DATE = "saved_date";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
    }

    @Override
    protected Fragment createFragment() {
        if (mFragment == null) {
            sPref = getPreferences(MODE_PRIVATE);
            mFragment = new ShopListFragment();
            ((ShopListFragment)mFragment).setSaveDate(sPref.getLong(SAVED_DATE, 0));
        }
        return mFragment;
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        saveDate();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    if (locationManager == null) {
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000 * 10, 10, locationListener);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                            locationListener);

                } else {
                    // permission denied
                    return;
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            mLat = location.getLatitude();
            mLng = location.getLongitude();
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            mLat = location.getLatitude();
            mLng = location.getLongitude();
        }
        ShopLab.get().setLat(mLat);
        ShopLab.get().setLng(mLng);
        ShopLab.get().updateLocation();

    }

    void saveDate() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(SAVED_DATE, new Date().getTime());
        ed.commit();
    }

}
