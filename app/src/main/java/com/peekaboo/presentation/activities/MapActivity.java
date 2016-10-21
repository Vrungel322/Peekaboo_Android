package com.peekaboo.presentation.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.peekaboo.R;

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
        addMarker();
    }


    private void addMarker(){
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("Marker")
                    .draggable(true)
            );
        }
    }


    /**
     * Initialises the mapview
     */
    private void createMapView(){

        try {
            if(null == googleMap){
//                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                        R.id.mapView)).getMapAsync(this);


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

        setUpMap();

    }


    public void setUpMap(){

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(50.459465,30.515301)).title("Peekaboo"));
//        googleMap.addMarker(new MarkerOptions().position()

    }
}