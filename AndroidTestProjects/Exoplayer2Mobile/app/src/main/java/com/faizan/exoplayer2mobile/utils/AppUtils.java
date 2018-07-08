package com.faizan.exoplayer2mobile.utils;

import android.app.Activity;
import android.util.Log;
import android.view.WindowManager;

public class AppUtils {

    private static final String TAG = "ExoMob AppUtils";

    public static void disableNotificationBar(Activity activity){
        String logMsg = "Disabling Notification bar for Activity: "+activity.getLocalClassName();
        Log.i(TAG, logMsg);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
