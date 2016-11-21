package com.peekaboo.presentation.activities;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    String markerPosLat, markerPosLng;
    Marker redmarker, bluemarker;

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();
    private FloatingActionButton fabpush, fabswitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmapstest);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createMapView();

        fabpush = (FloatingActionButton) findViewById(R.id.mapfab);
        fabswitch = (FloatingActionButton) findViewById(R.id.mapfabswitch);

        Toolbar mapToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
//        ab.setDisplayHomeAsUpEnabled(true);

//        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc).title("I'm here!").icon(
//                        BitmapDescriptorFactory.fromResource(R.drawable.locationbuble)));
//                if (googleMap != null) {
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18.0f));
//                }
//            }
//        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//       getMenuInflater().inflate(R.menu.activity_map_menu, (Menu) item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_back:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_del_markers:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_destination:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_send:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
                        .zoom(17)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
            }
        });
        //placed red marker
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (redmarker != null) {
                    redmarker.setPosition(latLng);
                    markerPosLat = String.valueOf(latLng.latitude);
                    markerPosLng = String.valueOf(latLng.longitude);

                } else {
//                    Log.d("MAPS_TAG", "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);

                    redmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).draggable(true).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.red_point)));//R.drawable.locationbuble
                    markerPosLat = String.valueOf(latLng.latitude);
                    markerPosLng = String.valueOf(latLng.longitude);
//                    Toast.makeText(getApplicationContext(), latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //when you placed red(destination) marker, generated link and send to smbd
        fabpush.setOnClickListener(v -> {
//            if (googleMap.getMyLocation() != null) {
            if (markerPosLng != "" && markerPosLat != "") {
                String lat = String.valueOf(googleMap.getMyLocation().getLatitude());//"50.459507";
                String lng = String.valueOf(googleMap.getMyLocation().getLongitude());//"30.514554";

                if (markerPosLng != "" && markerPosLat != "" && lat != "" && lng != "") {
                    String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng +
                            "&zoom=15&size=350x230" +
                            "&markers=icon:https://www.peekaboochat.com/assets/src/blue_point.png|" + lat + "," + lng +
                            "&markers=icon:https://www.peekaboochat.com/assets/src/red_point.png|" + markerPosLat + "," + markerPosLng +
                            "&path=color:0xff0000ff|weight:5|fillcolor:0xFFFF0033|geodesic:true|" + lat + "," + lng + "|" + markerPosLat + "," + markerPosLng +
                            "&sensor=true&scale=2";

                    Intent intent = new Intent();
                    intent.putExtra("staticmap", mapuri);
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }
//            }

        });
        //marker placed on your location
        fabswitch.setOnClickListener(v -> {
            if (googleMap.getMyLocation() != null) {
                if (bluemarker != null) {
                    bluemarker.setPosition(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()));
                } else {
                    bluemarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude())).draggable(true).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.locationbuble)));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(bluemarker.getPosition().latitude, bluemarker.getPosition().longitude))
                            .zoom(16)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    googleMap.animateCamera(cameraUpdate);
                }
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
        googleMap.getUiSettings().isMapToolbarEnabled();
    }
}