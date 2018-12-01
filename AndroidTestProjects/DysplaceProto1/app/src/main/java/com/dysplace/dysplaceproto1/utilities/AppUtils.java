package com.dysplace.dysplaceproto1.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.dysplace.dysplaceproto1.R;
import com.dysplace.dysplaceproto1.app_intro.ActivationActivity;
import com.dysplace.dysplaceproto1.app_intro.SplashActivity;
import com.dysplace.dysplaceproto1.main.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppUtils {

    private static final String LOG_TAG = "AppUtils";

    public static void goToActivationBasic(Activity activity){

        Log.i(LOG_TAG, "Going to ActivationActivity with basic intent");

        Intent intent = new Intent(activity, ActivationActivity.class);
        activity.startActivity(intent);
        activity.finishAffinity();
    }

    public static void goToMainBasic(Activity activity){

        Log.i(LOG_TAG, "Going to MainActivity with basic intent");

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finishAffinity();
    }

    public static void goToSplashBasic(Activity activity){

        Log.i(LOG_TAG, "Going to SplashActivity with basic intent");

        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
        activity.finishAffinity();
    }

    public static Dialog showWaitDialog(String message, Dialog waitDialog, Context context) {
        closeWaitDialog(waitDialog);
        waitDialog = new ProgressDialog(context);
        waitDialog.setTitle(message);
        waitDialog.show();
        return waitDialog;
    }

    public static void closeWaitDialog(Dialog waitDialog) {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    public static void makeLongToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showAppErrorDialog(Activity activity, String text){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(text).setTitle(R.string.dialog_title);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return reader.readLine();

        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public float getBatteryLevel(Activity activity){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = activity.registerReceiver(null, ifilter);
        int level = -1;
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }

        return level;
    }

    public static String getCurrentDateTime(){

        TimeZone tz = TimeZone.getTimeZone(VARIABLES.pakistanStandardTimeZone);
        Calendar c = Calendar.getInstance(tz);

        return String.format("%1$04d-%2$02d-%3$02d %4$02d:%5$02d:%6$02d", c.get(Calendar.YEAR),
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }
}
