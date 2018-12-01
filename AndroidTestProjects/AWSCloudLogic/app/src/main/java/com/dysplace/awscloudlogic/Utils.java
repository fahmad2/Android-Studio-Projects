package com.dysplace.awscloudlogic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

public class Utils {

    private static final String LOG_TAG = "Utils";
    private static final String SETTING_INFOS = "SETTING_Infos";
    private static final String userIdKey = "USER_ID";
    private static final String passwordKey = "PASSWORD";

    public static void disableNotificationBar(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static boolean isUserSignedIn(){
        return IdentityManager.getDefaultIdentityManager().isUserSignedIn();
    }

    public static boolean areCredentialsAvailable(Activity activity){

        Log.i(LOG_TAG, "Checking credentials");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(SETTING_INFOS, 0);

        return sharedPreferences.contains(userIdKey) && sharedPreferences.contains(passwordKey);
    }

    public static DysplaceCredentials getCredentials(Activity activity){

        SharedPreferences sharedPreferences = activity.getSharedPreferences(SETTING_INFOS, 0);

        String userID = sharedPreferences.getString(userIdKey, "");
        String password = sharedPreferences.getString(passwordKey, "");

        if(userID.equals("") || password.equals("")){
            return null;
        }

        return new DysplaceCredentials(userID, password);
    }

    public static void saveCredentials(String userID, String password, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SETTING_INFOS, 0);

        sharedPreferences.edit().putString(userIdKey, userID).putString(passwordKey, password).apply();
    }

    public static void clearCredentials(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SETTING_INFOS, 0);

        sharedPreferences.edit().clear().apply();
    }

    public static boolean isPasswordValid(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return password.matches(regex);
    }

//    public static boolean isCredJsonAvailable(Context context){
//        Field [] fields = R.raw.class.getFields();
//        String configFileName = context.getString(R.string.credential_json_name);
//
//        for(Field field: fields){
//            Log.i(LOG_TAG, "Field is: "+field.getName());
//            if(field.getName().equals(configFileName)){
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static DysplaceCredentials getRawJson(int jsonFileCode, Activity activity){
//        InputStream is;
//
//        try{
//            is = activity.getResources().openRawResource(jsonFileCode);
//        }
//        catch(Resources.NotFoundException nfe){
//            Log.i(LOG_TAG, nfe.getMessage());
//            return null;
//        }
//
//        Writer writer = new StringWriter();
//        char[] buffer = new char[1024];
//
//        try {
//            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            int n;
//            while ((n = reader.read(buffer)) != -1) {
//                writer.write(buffer, 0, n);
//            }
//        }
//        catch(IOException ioe){
//            Log.i(LOG_TAG, ioe.getMessage());
//        }
//        finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        String jsonString = writer.toString();
//
//        JSONObject jsonObject;
//
//        DysplaceCredentials dysplaceCredentials = null;
//
//        String userIdKey = activity.getString(R.string.user_id_json_key);
//        String passwordKey = activity.getString(R.string.password_json_key);
//
//        try {
//            jsonObject = new JSONObject(jsonString);
//            String userID = jsonObject.getString(userIdKey);
//            String password = jsonObject.getString(passwordKey);
//            dysplaceCredentials = new DysplaceCredentials(userID, password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//        return dysplaceCredentials;
//    }
//
//    public static void createDysCredJson(String userID, String password, Activity activity){
//        JSONObject jsonObject = new JSONObject();
//
//        String userIdKey = activity.getString(R.string.user_id_json_key);
//        String passwordKey = activity.getString(R.string.password_json_key);
//
//        try {
//            jsonObject.put(userIdKey, userID);
//            jsonObject.put(passwordKey, password);
//        } catch (JSONException e) {
//            Log.i(LOG_TAG, e.getMessage());
//            return;
//        }
//
//
//    }
}
