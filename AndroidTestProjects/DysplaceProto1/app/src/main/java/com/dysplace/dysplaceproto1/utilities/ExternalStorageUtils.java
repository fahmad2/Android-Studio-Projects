package com.dysplace.dysplaceproto1.utilities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalStorageUtils {

    private static final String LOG_TAG = "ExternalStorageUtils";

    public static void validateExtStoragePermissions(Activity activity){

        if(!isExternalStorageReadable()){

            String msgToast = "External Storage is not Readable. SD may not be inserted.";
            AppUtils.makeLongToast(activity, msgToast);

            return;
        }
        else if(!isExternalStorageWritable()){

            String msgToast = "External Storage is not Writable. SD may not be inserted.";
            AppUtils.makeLongToast(activity, msgToast);

            return;
        }

        String [] permissions = {VARIABLES.rdExtStrPermission, VARIABLES.wrtExtStrPermission};

        validatePermission(activity, permissions, VARIABLES.ExtStoragePermissionsRqtCode);
    }

    private static void validatePermission(Activity activity, String[] permissions, int requestCode) {
        Log.i(LOG_TAG, "Checking External Storage Permissions for API Level: "+
                Build.VERSION.SDK_INT);

        List<String> permissionsToRequest = new ArrayList<>();

        for(String permission: permissions){
            if (isPermissionGranted(activity, permission)) {
                Log.i(LOG_TAG,"Permission: "+permission+" is already granted");
            }
            else{
                Log.i(LOG_TAG,"Permission: "+permission+" not granted");
                permissionsToRequest.add(permission);
            }
        }

        String [] permissionsArray = new String[permissionsToRequest.size()];
        permissionsArray = permissionsToRequest.toArray(permissionsArray);


        if(permissionsArray.length > 0) {
            ActivityCompat.requestPermissions(activity, permissionsArray, requestCode);
        }
    }

    public static boolean isPermissionGranted(Activity activity, String permission){

        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static int validateDir(File dirPath){

        if(dirPath.exists() && !dirPath.isDirectory()){
            if(dirPath.delete()){
                Log.v(LOG_TAG, "Successfully deleted File: "+dirPath.getName());
            }
            else{
                Log.v(LOG_TAG, "Failed to delete File: "+dirPath.getName());
            }
        }

        if(!dirPath.exists()){
            Log.v(LOG_TAG, "Path: "+dirPath.getAbsolutePath()+" does not exist");

            if(dirPath.mkdirs()){
                Log.v(LOG_TAG, "Successfully created path: "+dirPath.getAbsolutePath());
            }
            else{
                Log.v(LOG_TAG, "Failed to create path: "+dirPath.getAbsolutePath());
                return VARIABLES.FAILURE_CODE;
            }
        }
        else{
            Log.v(LOG_TAG,"Path: "+dirPath.getAbsolutePath()+" exists");
        }

        return VARIABLES.SUCCESS_CODE;
    }

    public static int validateDysplaceLibraryStructure(){

        if(validateDir(VARIABLES.DYSPLACE_DIRECTORY) == VARIABLES.FAILURE_CODE){
            Log.i(LOG_TAG, "DYSPLACE directory not validated. Unable to validate " +
                    "Dysplace Library Structure");
            return VARIABLES.FAILURE_CODE;
        }

        if(validateDir(VARIABLES.DYS_ASSET_DIRECTORY) == VARIABLES.FAILURE_CODE){
            Log.i(LOG_TAG, "ASSETS directory not validated. Unable to validate " +
                    "Dysplace Library Structure");
            return VARIABLES.FAILURE_CODE;
        }

        if(validateDir(VARIABLES.DYS_TXT_DIRECTORY) == VARIABLES.FAILURE_CODE){
            Log.i(LOG_TAG, "TextFiles directory not validated. Unable to validate " +
                    "Dysplace Library Structure");
            return VARIABLES.FAILURE_CODE;
        }

        return VARIABLES.SUCCESS_CODE;
    }
}
