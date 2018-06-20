package developer.faizan.com.androidthingsexoplayertest.Utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by urfi on 3/27/18.
 */

public class AppUtils {
    private final String DEVICE_RPI3 = "rpi3";
    private static final String TAG= "AppUtils";

    public String getUartName() {
        Log.v(TAG, "Entered getUartName");
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                return "UART0";
            default:
                throw new IllegalStateException("Unsupported Build.DEVICE: " + Build.DEVICE);
        }
    }

    public boolean hasGPS(Context context){
        Log.v(TAG, "Entered hasGps");
        PackageManager pm = this.getContextPackageManager(context);
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public static String readFromFileSystem(String filePath, Character endChar){
        // Permit disk reads here, as /proc/meminfo isn't really "on
        // disk" and should be fast.  TODO: make BlockGuard ignore
        // /proc/ and /sys/ files perhaps?

        Log.v(TAG, "Entered readFromFileSystem");

        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();

        byte [] mBuffer = new byte[4096];
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            int len = is.read(mBuffer);
            is.close();

            if (len > 0) {
                int i;
                for (i=0; i<len; i++) {
                    if (mBuffer[i] == endChar) {
                        break;
                    }
                }
                return new String(mBuffer, 0, i);
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException Thrown");
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.d(TAG, "IOException Thrown");
                    e.printStackTrace();
                }
            }
            StrictMode.setThreadPolicy(savedPolicy);
        }
        return null;
    }

    private PackageManager getContextPackageManager(Context context){
        return context.getPackageManager();
    }
}
