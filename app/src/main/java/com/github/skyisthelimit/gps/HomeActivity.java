package com.github.skyisthelimit.gps;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends Activity {
    private Button changeHomeLocation;
    private TextView currentLocation;

    public static final int LOCATION_UPDATE_REQUEST_CODE = 0x0001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.changeHomeLocation = (Button) findViewById(R.id.change_location_button);
        this.changeHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(HomeActivity.this, LocationSelectionActivity.class);

                startActivityForResult(mapIntent, HomeActivity.LOCATION_UPDATE_REQUEST_CODE);
            }
        });

        this.currentLocation = (TextView) findViewById(R.id.current_location_text);
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
        // super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == HomeActivity.LOCATION_UPDATE_REQUEST_CODE) {
                Log.i("MAPSWHATEVER", "Received update from Mpas");
                if (data != null) {
                    if (data.getData() != null)
                        Log.i("MAPSWHATEVER", data.getDataString());
                }

                Bundle extras = data.getExtras();

                LatLng latLng = (LatLng) extras.get("location");

                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    Address address = addresses.get(0);

                    String addressText = "";

                    for(int i = 0;i < address.getMaxAddressLineIndex(); i++)
                        addressText += address.getAddressLine(i);

                    this.currentLocation.setText(addressText);
                } catch(IOException e) {

                }
            }
        }
    }
}
