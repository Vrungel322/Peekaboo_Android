package com.peekaboo.presentation.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.peekaboo.presentation.app.view.RoundedTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

//import static com.google.android.gms.wearable.DataMap.TAG;
import java.io.File;
import java.io.FileOutputStream;

import static com.peekaboo.R.id.imageView;


/**
 * Created by patri_000 on 18.10.2016.
 */

public class MapActivity extends Activity implements OnMapReadyCallback {

    GoogleMap googleMap;


    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmapstest);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createMapView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mapfab);
        fab.setOnClickListener(v -> {
            Toast.makeText(this, googleMap.getMyLocation().getLatitude() + " " + googleMap.getMyLocation().getLongitude(), Toast.LENGTH_LONG).show();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()))
                    .zoom(10)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);

            String lat = String.valueOf(googleMap.getMyLocation().getLatitude());//"50.459507";
            String lng = String.valueOf(googleMap.getMyLocation().getLongitude());//"30.514554";
            String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=18&size=350x230&sensor=true";
//            Picasso.with(this).load(mapuri).into((ImageView)findViewById(R.id.testimgview));
//            Picasso.with(this).load(mapuri).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            File file = new File(
//                                    Environment.getExternalStorageDirectory().getPath()
//                                            + "/saved.jpg");
//                            try {
//                                file.createNewFile();
//                                FileOutputStream ostream = new FileOutputStream(file);
//                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
//                                ostream.close();
//                            }
//                            catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//
//
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });
            Intent intent = new Intent();
            intent.putExtra("staticmap", mapuri);
            setResult(RESULT_OK, intent);
//            Log.wtf("NULL : ","sendim gpsimg in fragment");

            finish();

        });
//            new DownloadImageTask((ImageView) findViewById(R.id.testimgview))
//                    .execute(url);
//            Picasso.with(this).load(url).into((ImageView)findViewById(R.id.testimgview));
//            mPicasso.load(url).resizeDimen(R.dimen.chat_image_width, R.dimen.chat_image_height)
//                    .error(R.drawable.ic_alert_circle_outline)
//                    .centerInside()
//                    .transform(new RoundedTransformation(25, 0))
//                    .into(holder.ivImageMessage, new Callback.EmptyCallback() {
//                        @Override
//                        public void onSuccess() {
//                            holder.pbLoadingImage.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            holder.pbLoadingImage.setVisibility(View.GONE);
//                        }
//
//                    });
//        });
//    }

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