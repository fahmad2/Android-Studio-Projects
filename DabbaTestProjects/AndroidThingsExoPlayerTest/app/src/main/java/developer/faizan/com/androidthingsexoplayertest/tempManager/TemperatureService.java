package developer.faizan.com.androidthingsexoplayertest.tempManager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by urfi on 3/22/18.
 */

public class TemperatureService extends Service {

    private Handler handler = null;
    private static Runnable runnable = null;
    private CpuTempManagerExposed temperatureManager;
    private CpuTempManagerUnexposed tempManagerUnexposed;
    private final String TAG = "TemperatureService";
    private final boolean tempExposed = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                float temp;

                if (tempExposed) {
                    temp = getTempExposed();
                } else {
                    temp = getTempUnexposed();
                }
                String logMsg = "The temperature noted is: "+String.valueOf(temp)+" Celsius";
                Log.d(TAG, logMsg);
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    private float getTempExposed(){
        try {
            temperatureManager = new CpuTempManagerExposed(this);
        } catch (SensorManagerNotFound sensorManagerNotFound) {
            sensorManagerNotFound.printStackTrace();
        }

        return temperatureManager.getCurrentTemperature();
    }

    private float getTempUnexposed(){
        tempManagerUnexposed = new CpuTempManagerUnexposed();

        return tempManagerUnexposed.getTemperature();
    }
}
