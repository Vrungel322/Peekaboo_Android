package com.peekaboo.presentation.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.peekaboo.R;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by patri_000 on 18.10.2016.
 */

public class MapActivity extends Activity implements OnMapReadyCallback {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmapstest);
        createMapView();
//        addMarker();
    }
//    private void addMarker(){
//        if(null != googleMap){
//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(0, 0))
//                    .title("Marker")
//                    .draggable(true)
//            );
//        }
//    }


    /**
     * Initialises the mapview
     */
    private void createMapView(){

        try {
            if(null == googleMap){
                MapFragment googleMap = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.mapView);

                googleMap.getMapAsync(this);

                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
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
                Log.d(TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(50.459465,30.515301))
                        .zoom(19)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)).draggable(true).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.locationbuble)));

            }
        });

//        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//
//            @Override
//            public void onCameraChange(CameraPosition camera) {
//                Log.d(TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
//            }
//        });
    }

    public void setUpMap(){

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(50.459465,30.515301)).title("Peekaboo").draggable(true));
//        googleMap.addMarker(new MarkerOptions().position()


    }
}