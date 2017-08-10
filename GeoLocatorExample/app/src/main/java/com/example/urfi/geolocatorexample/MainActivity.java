package com.example.urfi.geolocatorexample;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.textView)).setMovementMethod(new ScrollingMovementMethod());
    }

    public void doLatLon(View v) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(
                    41.8764396,
                    -87.6343844,
                    10);
            displayAddresses(addresses);
            Log.d(TAG, "onCreate: " + addresses.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doLocationName(View v) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;

            String loc = ((EditText) findViewById(R.id.editText)).getText().toString();
            addresses = geocoder.getFromLocationName(loc, 1);
            displayAddresses(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        if (addresses.size() == 0) {
            ((TextView) findViewById(R.id.textView)).setText("Nothing Found");
            return;
        }

        for (Address address : addresses) {
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i) + "\n");
            }
            sb.append("\n");
        }
        ((TextView) findViewById(R.id.textView)).setText(sb.toString());
    }
}