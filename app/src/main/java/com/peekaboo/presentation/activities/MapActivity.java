package com.peekaboo.presentation.activities;

import android.content.Intent;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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

/**
 * Created by patri_000 on 18.10.2016.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    String markerPosLat, markerPosLng;
    Marker redmarker, bluemarker;

    private LocationManager locationManager;

    private FloatingActionButton fabpush, fabswitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmapstest);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createMapView();

//        fabpush = (FloatingActionButton) findViewById(R.id.mapfab);
        fabswitch = (FloatingActionButton) findViewById(R.id.mapfabswitch);

        Toolbar mapToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_push:
                push();
                return true;

            case R.id.action_search:
                try {
                    Intent autocomplete =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(autocomplete, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void push() {
        if (bluemarker != null && redmarker != null && bluemarker.isVisible() && redmarker.isVisible()) {

            double lat = (bluemarker.getPosition().latitude + redmarker.getPosition().latitude) / 2;
            double lng = (bluemarker.getPosition().longitude + redmarker.getPosition().longitude) / 2;

            String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng +
                    "&markers=icon:https://www.peekaboochat.com/assets/src/red_point.png|" + redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                    "&markers=icon:https://www.peekaboochat.com/assets/src/blue_point.png|" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude +
                    "&path=color:0xff0000ff|weight:5|fillcolor:0xFFFF0033|geodesic:true|" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude + "|" +
                    redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                    "&zoom=15&size=350x230" +
                    "&sensor=true&scale=2";

            Intent intent = new Intent();
            intent.putExtra("staticmap", mapuri);
            setResult(RESULT_OK, intent);
            Log.wtf("NULL : ", "send gps from activity!!!!!"+mapuri);

            finish();


        } else {
            if (bluemarker != null && bluemarker.isVisible()) {
                String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude +
                        "&markers=icon:https://www.peekaboochat.com/assets/src/blue_point.png|" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude +
                        "&zoom=15&size=350x230" + "&sensor=true&scale=2";
                Intent intent = new Intent();
                intent.putExtra("staticmap", mapuri);
                setResult(RESULT_OK, intent);
                Log.wtf("NULL : ", "send gps from activity!!!!!");

                finish();
            } else {
                if (redmarker != null && redmarker.isVisible()) {
                    String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                            "&markers=icon:https://www.peekaboochat.com/assets/src/red_point.png|" + redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                            "&zoom=15&size=350x230" + "&sensor=true&scale=2";
                    Intent intent = new Intent();
                    intent.putExtra("staticmap", mapuri);
                    setResult(RESULT_OK, intent);
                    Log.wtf("NULL : ", "send gps from activity!!!!!");

                    finish();
                }


            }

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
                Toast.makeText(getApplicationContext(),"Make long click on map to place destination marker.",Toast.LENGTH_LONG).show();

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

        googleMap.setOnInfoWindowClickListener(redmarker -> {
            if (redmarker != null) {
                redmarker.setVisible(false);
                markerPosLng = "";
                markerPosLat = "";
            }
        });

        googleMap.setOnInfoWindowClickListener(bluemarker -> {

            if (bluemarker != null) {
                bluemarker.setVisible(false);
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("MAPS_TAG", "onMapClick: " + latLng.latitude + "," + latLng.longitude);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latLng.latitude, latLng.longitude))
                        .zoom(16)
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
                    redmarker.setVisible(true);
                    markerPosLat = String.valueOf(latLng.latitude);
                    markerPosLng = String.valueOf(latLng.longitude);

                } else {
                    redmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_point))
                            .title("Delete marker?"));
                    markerPosLat = String.valueOf(latLng.latitude);
                    markerPosLng = String.valueOf(latLng.longitude);
                }
            }
        });



        //marker placed on your location
        fabswitch.setOnClickListener(v -> {
            if (googleMap.getMyLocation() != null) {
                if (bluemarker != null) {
                    bluemarker.setVisible(true);
                    bluemarker.setPosition(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()));
                } else {
                    bluemarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationbuble))
                            .title("Delete marker?"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(bluemarker.getPosition().latitude, bluemarker.getPosition().longitude))
                            .zoom(16)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    googleMap.animateCamera(cameraUpdate);
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .zoom(16)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void setUpMap() {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().isMapToolbarEnabled();
    }
}