package developer.faizan.com.androidthingsexoplayertest.tempManager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by urfi on 3/22/18.
 */

@Deprecated
public class CpuTempManagerExposed implements SensorEventListener{

    private final SensorManager sensorManager;
    private final Sensor tempSensor;
    private final String TAG = "CpuTempManagerExposed";
    private float currentTemperature;

    CpuTempManagerExposed(Context mContext) throws SensorManagerNotFound {

        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);


        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        else {
            tempSensor = null;
            throw new SensorManagerNotFound();
        }

        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String logMsg = "The temperature noted is: "+String.valueOf(currentTemperature)+" Celsius";
        Log.d(TAG, logMsg);
        this.currentTemperature = event.values[0];
        Log.d(TAG, "The event: "+event.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    float getCurrentTemperature(){
        return this.currentTemperature;
    }
}
