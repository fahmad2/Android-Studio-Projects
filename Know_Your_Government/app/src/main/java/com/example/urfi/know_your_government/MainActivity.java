package com.example.urfi.know_your_government;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public List<Official> officialList = new ArrayList<Official>();
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private OfficialAdapter oAdapter;
    private LocationManager locationManager;
    public Official currentOfficial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("No Network Connection");
        builder1.setMessage("Data can not be accessed/loaded without Internet connection.");
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();

        boolean bol1 = true;

        while (!isConnected()) {
            if (bol1 == true) {
                alert11.show();
                bol1 = false;
                return;
            }
        }

        alert11.cancel();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        oAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(oAdapter);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        Location loc = this.getLocation();
        TextView tv = (TextView) findViewById(R.id.locationName);

        doAsyncLoad(tv.getText().toString());
    }

    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        Official offc = officialList.get(pos);

        this.setCurrentOfficial(offc);

        Intent i = new Intent(MainActivity.this, OfficialActivity.class);

        String loc = ((TextView) findViewById(R.id.locationName)).getText().toString();

        i.putExtra("loc", loc);
        i.putExtra("Official", offc);

        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_about:
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
            case R.id.app_bar_search:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Enter a City, State or Zip Code");

                final EditText input = new EditText(MainActivity.this);
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;

                        TextView loc = (TextView) findViewById(R.id.locationName);
                        String in = input.getText().toString();
                        if (in.length() > 0) {
                            try {
                                addresses = geocoder.getFromLocationName(in, 1);
                                if(addresses.size() > 0){
                                    loc.setText(addresses.get(0).getAddressLine(0));
                                    doAsyncLoad(loc.getText().toString());
                                }
                                else if(addresses.size() == 0){
                                    loc.setText("Invalid Location");
                                    officialList.clear();
                                    oAdapter.notifyDataSetChanged();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? false : networkInfo.isConnected();
    }

    public void setCityName(Location loc){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 10);

            TextView tv = (TextView) findViewById(R.id.locationName);

            tv.setText(addresses.get(0).getAddressLine(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//     This method asks the user for Locations permissions (if it does not already have them)
    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

    public Location getLocation() {

        if (!checkPermission())
            return null;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                this.setCityName(loc);
                return loc;
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 100.00f, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        MainActivity.this.setCityName(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }

        return null;
    }

    public void doAsyncLoad(String zip){
        AsyncOfficialDataLoader alt = new AsyncOfficialDataLoader(this);
        alt.execute(zip);
    }

    public void setCurrentOfficial(Official o){
        this.currentOfficial = o;
    }

    public Official getCurrentOfficial(){
        return this.currentOfficial;
    }

    public void updateOffList(List<Map<String, String>> list){
        officialList.clear();

        for(Map<String, String> mp :list){
            Official ofl = new Official(mp.get("name"), mp.get("office"));


            String address = "";
            if(mp.containsKey("line1")){
                address = address+mp.get("line1")+"\n";
            }
            if(mp.containsKey("line2")){
                address = address+mp.get("line2")+"\n";
            }
            if(mp.containsKey("city")){
                address = address+mp.get("city")+" ";
            }
            if(mp.containsKey("state")){
                address = address+mp.get("state")+" ";
            }
            if(mp.containsKey("zip")){
                address = address+mp.get("zip");
            }
            ofl.setAddress(address);

            if(mp.containsKey("party")){
                String party = mp.get("party");
                if(party.equalsIgnoreCase("democratic")){
                    ofl.setParty(Official.PoliticalParty.DEMOCRATIC);
                }
                else if(party.equalsIgnoreCase("republican")){
                    ofl.setParty(Official.PoliticalParty.REPUBLICAN);
                }
                else {
                    ofl.setParty(Official.PoliticalParty.NEITHER);
                }
            }
            else{
                ofl.setParty(Official.PoliticalParty.NEITHER);
            }

            if(mp.containsKey("phone")){
                ofl.setPhone(mp.get("phone"));
            }

            if(mp.containsKey("website")){
                ofl.setWebsite(mp.get("website"));
            }

            if(mp.containsKey("email")){
                ofl.setEmail(mp.get("email"));
            }

            if(mp.containsKey("photo")){
                ofl.setPhoto(mp.get("photo"));
            }

            if(mp.containsKey("GooglePlus")){
                ofl.setgPlus(mp.get("GooglePlus"));
            }

            if(mp.containsKey("Facebook")){
                ofl.setFaceb(mp.get("Facebook"));
            }

            if(mp.containsKey("Twitter")){
                ofl.setTwit(mp.get("Twitter"));
            }

            if(mp.containsKey("Youtube")){
                ofl.setYout(mp.get("Youtube"));
            }

            officialList.add(ofl);
        }
        oAdapter.notifyDataSetChanged();
    }
}
