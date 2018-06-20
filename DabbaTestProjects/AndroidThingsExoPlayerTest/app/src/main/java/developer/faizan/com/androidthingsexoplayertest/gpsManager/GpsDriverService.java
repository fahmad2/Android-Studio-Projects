package developer.faizan.com.androidthingsexoplayertest.gpsManager;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.things.userdriver.UserDriverManager;
//import com.google.android.things.userdriver.location.GpsDriver;

/**
 * Created by urfi on 3/25/18.
 */

public class GpsDriverService extends Service {

//    private static GpsDriver mDriver;
//    private Handler handler = null;
//    private static Runnable runnable = null;
//    private final String TAG = "GpsDriverService";
//
//    private LocationManager locationManager;
//
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//
//    @Override
//    public void onCreate() {
//        Log.d(TAG, "Entered onCreate");
//        super.onCreate();
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // A problem occurred auto-granting the permission
//            Log.d(TAG, "No permission");
//            return;
//        }
//
//        // Create a new driver implementation
//        this.registerGpsDriver();
//
//        handler = new Handler();
//        runnable = new Runnable() {
//            public void run() {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
//                handler.postDelayed(runnable, 10000);
//            }
//        };
//
//        handler.postDelayed(runnable, 15000);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        this.unregisterGpsDriver();
//    }
//
//    private void registerGpsDriver(){
//        // Create a new driver implementation
//        mDriver = new GpsDriver();
//
//        // Register with the framework
//        UserDriverManager manager = UserDriverManager.getInstance();
//        manager.registerGpsDriver(mDriver);
//    }
//
//    private void unregisterGpsDriver(){
//        UserDriverManager manager = UserDriverManager.getInstance();
//        manager.unregisterGpsDriver();
//    }
//
//    private Location parseLocationFromString(String gpsData) {
//        Location result = new Location(LocationManager.GPS_PROVIDER);
//
//        //parse gpsData
//
//        //required
////        result.setAccuracy( getAccuracyFromGpsData( gpsData ) );
////        result.setTime( getTimeFromGpsData( gpsData ) );
////        result.setLatitude( getLatitudeFromGpsData( gpsData ) );
////        result.setLongitude( getLongitudeFromGpsData( gpsData ) );
////
////        //optional
////        result.setAltitude( getAltitudeFromGpsData( gpsData ) );
////        result.setBearing( getBearingFromGpsData( gpsData ) );
////        result.setSpeed( getSpeedFromGpsData( gpsData ) );
//
//        return result;
//    }
//
//    private LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.v(TAG, "Location update: " + location);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) { }
//
//        @Override
//        public void onProviderEnabled(String provider) { }
//
//        @Override
//        public void onProviderDisabled(String provider) { }
//    };

}
