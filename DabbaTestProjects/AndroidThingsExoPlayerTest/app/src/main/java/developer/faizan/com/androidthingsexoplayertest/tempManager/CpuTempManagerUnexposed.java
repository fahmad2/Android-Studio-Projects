package developer.faizan.com.androidthingsexoplayertest.tempManager;

import android.util.Log;

import developer.faizan.com.androidthingsexoplayertest.Utilities.AppUtils;

/**
 * Created by urfi on 3/27/18.
 */

class CpuTempManagerUnexposed {

    private final String TAG = "CpuTempManagerUnexposed";
    private final String temperatureFile = "/sys/class/thermal/thermal_zone0/temp";

    CpuTempManagerUnexposed(){}

    Float getTemperature(){
        Log.v(TAG, "Fetching CPU temperature");
        String tempStr = AppUtils.readFromFileSystem(temperatureFile, '\n');
        return Float.parseFloat(tempStr)/1000f;
    }
}
