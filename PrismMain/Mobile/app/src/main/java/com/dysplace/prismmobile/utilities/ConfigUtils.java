package com.dysplace.prismmobile.utilities;

import android.app.Activity;
import android.view.WindowManager;

public class ConfigUtils {

    public static void disableNotificationBar(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
