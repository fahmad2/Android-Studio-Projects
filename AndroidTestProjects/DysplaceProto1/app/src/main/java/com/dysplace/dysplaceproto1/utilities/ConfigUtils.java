package com.dysplace.dysplaceproto1.utilities;

import android.app.Activity;
import android.util.Log;
import android.view.WindowManager;

public class ConfigUtils {

    private static final String LOG_TAG = "ConfigUtils";

    public static void configureActivity(Activity activity){
        disableNotificationBar(activity);
        keepScreenOn(activity);
    }

    private static void disableNotificationBar(Activity activity){

        Log.i(LOG_TAG, "Disabling Notification Bar in Activity: "
                +activity.getLocalClassName());

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private static void keepScreenOn(Activity activity){
        Log.i(LOG_TAG, "Keeping Screen On in Activity: "
                +activity.getLocalClassName());

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
