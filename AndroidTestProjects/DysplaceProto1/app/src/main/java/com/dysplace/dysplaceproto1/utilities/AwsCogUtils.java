package com.dysplace.dysplaceproto1.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.dysplace.dysplaceproto1.pojos.user_authorization.DysCredentials;

public class AwsCogUtils {

    private static final String LOG_TAG = "AwsCogUtils";

    public static void addSignInStateChangelistener(Activity activity){
        IdentityManager.getDefaultIdentityManager().addSignInStateChangeListener(new SignInStateChangeListener() {
            @Override
            // Sign-in listener
            public void onUserSignedIn() {
                Log.i(LOG_TAG, "User Signed In");
                //AppUtils.goToMainBasic(activity);
            }

            // Sign-out listener
            @Override
            public void onUserSignedOut() {
                Log.i(LOG_TAG, "User Signed Out");
                AppUtils.goToSplashBasic(activity);
            }
        });
    }

    public static boolean isUserSignedIn(){
        return IdentityManager.getDefaultIdentityManager().isUserSignedIn();
    }

    public static void signOutUser(){
        IdentityManager.getDefaultIdentityManager().signOut();
    }

    public static boolean areCredentialsAvailable(Activity activity){

        Log.i(LOG_TAG, "Checking credentials");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                VARIABLES.SETTING_INFOS, 0);

        return sharedPreferences.contains(VARIABLES.userIdKey) &&
                sharedPreferences.contains(VARIABLES.passwordKey);
    }

    public static DysCredentials getCredentials(Activity activity){

        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                VARIABLES.SETTING_INFOS, 0);

        String userID = sharedPreferences.getString(VARIABLES.userIdKey, "");
        String password = sharedPreferences.getString(VARIABLES.passwordKey, "");

        if(userID.equals("") || password.equals("")){
            return null;
        }

        return new DysCredentials(userID, password);
    }

    public static void saveCredentials(String userID, String password, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                VARIABLES.SETTING_INFOS, 0);

        sharedPreferences.edit().putString(VARIABLES.userIdKey, userID).
                putString(VARIABLES.passwordKey, password).apply();
    }

    public static void clearCredentials(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                VARIABLES.SETTING_INFOS, 0);

        sharedPreferences.edit().clear().apply();
    }

    public static boolean isPasswordValid(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return password.matches(regex);
    }
}
