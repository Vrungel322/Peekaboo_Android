package com.peekaboo.presentation.activities;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.peekaboo.R;

//import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * Created by patri_000 on 18.10.2016.
 */

public class MapActivity extends Activity implements OnMapReadyCallback {

    GoogleMap googleMap;


    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmapstest);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createMapView();

        fab = (FloatingActionButton) findViewById(R.id.mapfab);

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc).title("I'm here!").icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.locationbuble)));
                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18.0f));
                }
            }
        };
    }

    /**
     * Initialises the mapview
     */
    private void createMapView() {

        try {
            if (null == googleMap) {
                MapFragment googleMap = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.mapView);
                googleMap.getMapAsync(this);

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("navigation", exception.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        init();
        setUpMap();
        fab.setOnClickListener(v -> {
            Toast.makeText(this, googleMap.getMyLocation().getLatitude() + " " + googleMap.getMyLocation().getLongitude(), Toast.LENGTH_LONG).show();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()))
                    .zoom(10)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);

            String lat = String.valueOf(googleMap.getMyLocation().getLatitude());//"50.459507";
            String lng = String.valueOf(googleMap.getMyLocation().getLongitude());//"30.514554";
            String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng +
                    "&zoom=18&size=350x230" + "&markers=icon:http://chart.apis.google.com/chart?chst=d_map_pin_icon|" + lat + "," + lng +
                    "&sensor=true";
            Intent intent = new Intent();
            intent.putExtra("staticmap", mapuri);
            setResult(RESULT_OK, intent);

            finish();

        });

    }


    private void init() {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("MAPS_TAG", "onMapClick: " + latLng.latitude + "," + latLng.longitude);
//                double altitude = location.getAltitude();
//                double longtitude = location.getLongitude();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latLng.latitude, latLng.longitude))
                        .zoom(19)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d("MAPS_TAG", "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).draggable(true).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.locationbuble)));

            }
        });

    }

    public void setUpMap() {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}