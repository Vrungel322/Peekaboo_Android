package com.peekaboo.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.peekaboo.data.repositories.database.contacts.Contact;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Intent intent = getIntent();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createMapView();

        fabswitch = (FloatingActionButton) findViewById(R.id.mapfabswitch);

        Toolbar mapToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

//        getextra();
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void getextra() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String link = intent.getStringExtra("mesmap");
            Log.wtf("NULL : ", "get intent -" + link + "____" + link.length());
            Contact contact = intent.getExtras().getParcelable("contact");
            Log.wtf("NULL : ", "get contact image -" + contact.contactImgUriSmall());


            if (link.length() < 300) {
                String regexp = "[\\=|\\|]([-+]?\\d*\\.\\d+(.*?))\\&";
                Pattern pattern = Pattern.compile(regexp);
                Matcher matcher = pattern.matcher(link);
                System.out.println(matcher.find() ? "I found0 '" + matcher.group() : "I found nothing!");
                String result = matcher.group();

                String reg = "(-?(?:\\d*\\.)?\\d+)(-?(?:\\d*\\.)?\\d+)";
                Pattern pattern1 = Pattern.compile(reg);
                Matcher matcher1 = pattern1.matcher(result);

                System.out.println(matcher1.find() ? "I found1 '" + matcher1.group(0) : "I found nothing!");
                double lat = Double.parseDouble(matcher1.group(0));
                System.out.println(matcher1.find() ? "I found2 '" + matcher1.group(1) : "I found nothing!");
                double lng = Double.parseDouble(matcher1.group(1));

                Log.wtf("NULL : ", matcher1.group(0) + "<-lat|matcher|lng->" + matcher1.group(1));
                Log.wtf("NULL : ", lat + "<-lat||lng->" + lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(16)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);

                if (redmarker != null) {
                    redmarker.setPosition(new LatLng(lat, lng));
                    redmarker.setVisible(true);
                    markerPosLat = String.valueOf(lat);
                    markerPosLng = String.valueOf(lng);
                } else {
                    redmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_point))
                            .title("Delete marker?"));
                    markerPosLat = String.valueOf(lat);
                    markerPosLng = String.valueOf(lng);
                }
            } else {

                String regexp = "[\\|\\|]([-+]?\\d*\\.\\d+(.*?))\\&";
                Pattern pattern = Pattern.compile(regexp);
                Matcher matcher = pattern.matcher(link);
                System.out.println(matcher.find() ? "I found group1 " + matcher.group(0) : "I found nothing!");

                String result = matcher.group();
                String reg = "(-?(?:\\d*\\.)?\\d+)(-?(?:\\d*\\.)?\\d+)";
                Pattern pattern1 = Pattern.compile(reg);
                Matcher matcher1 = pattern1.matcher(result);

                System.out.println(matcher1.find() ? "I found1 '" + matcher1.group(0) : "I found nothing!");
                double lat = Double.parseDouble(matcher1.group(0));
                System.out.println(matcher1.find() ? "I found2 '" + matcher1.group(1) : "I found nothing!");
                double lng = Double.parseDouble(matcher1.group(1));

                Log.wtf("NULL : ", matcher1.group(0) + "<-lat|matcher|lng->" + matcher1.group(1));
                Log.wtf("NULL : ", lat + "<-lat||lng->" + lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(16)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);

                if (redmarker != null) {
                    redmarker.setPosition(new LatLng(lat, lng));
                    redmarker.setVisible(true);
                    markerPosLat = String.valueOf(lat);
                    markerPosLng = String.valueOf(lng);
                } else {
                    redmarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_point))
                            .title("Delete marker?"));
                    markerPosLat = String.valueOf(lat);
                    markerPosLng = String.valueOf(lng);
                }

                System.out.println(matcher.find() ? "I found group2 " + matcher.group(1) : "I found nothing!");
                result = matcher.group(1);
                matcher1 = pattern1.matcher(result);
                System.out.println(matcher1.find() ? "I found1 '" + matcher1.group(0) : "I found nothing!");
                lat = Double.parseDouble(matcher1.group(0));
                System.out.println(matcher1.find() ? "I found2 '" + matcher1.group(1) : "I found nothing!");
                lng = Double.parseDouble(matcher1.group(1));
                Log.wtf("NULL : ", matcher1.group(0) + "<-lat|matcher|lng->" + matcher1.group(1));
                Log.wtf("NULL : ", lat + "<-lat||lng->" + lng);
                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(16)
                        .build();
                cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title("Delete marker?")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.avatar))));
            }


        }
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
            Log.wtf("NULL : ", "NEW PUSH " + lat + "  |  " + lng);


            String mapuri = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng +
                    "&markers=icon:https://www.peekaboochat.com/assets/src/blue_point.png|" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude +
                    "&markers=icon:https://www.peekaboochat.com/assets/src/red_point.png|" + redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                    "&path=color:0xff0000ff|weight:5|fillcolor:0xFFFF0033|geodesic:true|" + bluemarker.getPosition().latitude + "," + bluemarker.getPosition().longitude + "|" +
                    redmarker.getPosition().latitude + "," + redmarker.getPosition().longitude +
                    "&zoom=15&size=350x230" +
                    "&sensor=true&scale=2";

            Intent intent = new Intent();
            intent.putExtra("staticmap", mapuri);
            setResult(RESULT_OK, intent);
            Log.wtf("NULL : ", "send gps from activity!!!!!" + mapuri);

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
                Toast.makeText(getApplicationContext(), "Make long click on map to place destination marker.", Toast.LENGTH_LONG).show();

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
//                getextra();
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
        getextra();
    }
}