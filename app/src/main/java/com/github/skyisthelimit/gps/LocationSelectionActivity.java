package com.github.skyisthelimit.gps;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationSelectionActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
        /*if(event.getAction() == MotionEvent.ACTION_UP)
        {
            HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, HelloGoogleMaps.this);
            GeoPoint p = mapView.getProjection().fromPixels(
                    (int) event.getX(), (int) event.getY());
            String lat = Double.toString(p.getLatitudeE6() / (Math.pow(10,6)));
            String lon = Double.toString(p.getLongitudeE6() / (Math.pow(10,6)));
            OverlayItem overlayitem = new OverlayItem(p, "Title text", "Body Text");
            itemizedoverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedoverlay);
            map.postInvalidate();
            Intent i = new Intent();
            i.putExtra("latitude", Double.parseDouble(lat));
            i.putExtra("longitude", Double.parseDouble(lon));
            this.setResult(RESULT_OK, i);
            finish();
            return true;
        }*/
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        setUpMapIfNeeded();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("MAPCLICK", latLng.latitude + " : " + latLng.longitude + " : " + latLng.toString());

                Intent result = new Intent();
                result.putExtra("location", latLng);

                setResult(HomeActivity.LOCATION_UPDATE_REQUEST_CODE, result);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
