package com.github.skyisthelimit.gps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity {
    private TextView homeLocationText;
    private TextView currentLocationText;
    private TextView currentStatusText;
    private TextView distanceText;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 100;

    private LocationManager locationManager;

    public static final int LOCATION_UPDATE_REQUEST_CODE = 0x0001;
    public static final int RADIUS_TO_CHECK_FOR = 100;

    private LatLng homeLocation;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button changeHomeLocation = (Button) findViewById(R.id.change_location_button);
        changeHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(HomeActivity.this, LocationSelectionActivity.class);

                startActivityForResult(mapIntent, HomeActivity.LOCATION_UPDATE_REQUEST_CODE);
            }
        });

        this.homeLocationText = (TextView) findViewById(R.id.home_location_text);
        this.currentLocationText = (TextView) findViewById(R.id.current_location_text);
        this.distanceText = (TextView) findViewById(R.id.distance_text);
        this.currentStatusText = (TextView) findViewById(R.id.current_status_text);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

        this.homeLocation = new LatLng(41.8789, 87.6358);

        this.refreshLocationDisplays();
    }

    private void refreshLocationDisplays() {
        if(this.homeLocation != null && this.homeLocationText != null) {
            this.homeLocationText.setText(this.homeLocation.latitude + ", " + this.homeLocation.longitude);
        }

        if(this.currentLocation != null && this.currentLocationText != null) {
            this.currentLocationText.setText(this.currentLocation.latitude + ", " + this.currentLocation.longitude);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == HomeActivity.LOCATION_UPDATE_REQUEST_CODE) {
                Bundle extras = data != null ? data.getExtras() : null;

                this.homeLocation = (LatLng) (extras != null ? extras.get("location") : null);
                this.refreshLocationDisplays();
            }
        }
    }

    private void calculateDistanceAndComputeStatus() {
        final double distance = SphericalUtil.computeDistanceBetween(this.homeLocation, this.currentLocation);

        if(distance > RADIUS_TO_CHECK_FOR)
            this.currentStatusText.setText("NOT AT HOME");
        else
            this.currentStatusText.setText("AT HOME");

        this.distanceText.setText("Distance: " + (Math.round(distance * Math.pow(10, 4)) / Math.pow(10, 4)) + "m");

        Toast.makeText(this, "This is a probably a good time to inform our servers about this activity.", Toast.LENGTH_LONG).show();
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            currentLocationText.setText(currentLocation.latitude + ", " + currentLocation.longitude);

            calculateDistanceAndComputeStatus();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(HomeActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(HomeActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(HomeActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }
    }
}
