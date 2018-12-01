package com.dysplace.dysplaceproto1.app_intro;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.dysplace.dysplaceproto1.R;
import com.dysplace.dysplaceproto1.interfaces.user_authorization.AwsCogActivity;
import com.dysplace.dysplaceproto1.interfaces.aws_mobile_client.AwsMobHubActivity;
import com.dysplace.dysplaceproto1.utilities.ExternalStorageUtils;
import com.dysplace.dysplaceproto1.pojos.user_authorization.DysCredentials;
import com.dysplace.dysplaceproto1.utilities.AppUtils;
import com.dysplace.dysplaceproto1.utilities.AwsCogUtils;
import com.dysplace.dysplaceproto1.utilities.ConfigUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;

public class SplashActivity extends AppCompatActivity implements AwsMobHubActivity, AwsCogActivity {

    private static final String LOG_TAG = "SplashActivity";
    private static final int SPLASH_TIME_OUT = 2000;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);
        }
        catch (RuntimeException re){
            Log.e(LOG_TAG, re.getMessage());
            Log.i(LOG_TAG, "Exiting App");
            finishAndRemoveTask();
        }

        ConfigUtils.configureActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initApp();
    }

    @Override
    public void onConnectToAWS() {

        int result = ExternalStorageUtils.validateDysplaceLibraryStructure();

        if(result == VARIABLES.FAILURE_CODE){
            AppUtils.showAppErrorDialog(this, "Unable to validate " +
                    "Dysplace Library Structure");
            return;
        }

        if(AwsCogUtils.isUserSignedIn()){
            Log.i(LOG_TAG, "User is already signed in");

            AppUtils.goToMainBasic(this);
        }
        else{
            Log.i(LOG_TAG, "User not signed in");

            if(AwsCogUtils.areCredentialsAvailable(this)){
                Log.i(LOG_TAG, "Device has already been activated");
                DysCredentials dysplaceCredentials = AwsCogUtils.getCredentials(this);
                if(dysplaceCredentials == null){
                    Log.i(LOG_TAG, "Failed to get credentials. Going to Sign in page");
                    AppUtils.goToActivationBasic(this);
                }
                else{
                    String userID = dysplaceCredentials.getUserID();
                    String password = dysplaceCredentials.getPassword();
                    if(userID != null && password != null){
                        Log.i(LOG_TAG, "Credentials are eligible");
                        signInUser(userID, password, this);
                    }
                    else{
                        Log.i(LOG_TAG, "Saved Credentials are ineligible");
                        AppUtils.goToActivationBasic(this);
                    }
                }
            }
            else{
                Log.i(LOG_TAG, "Device activation is required");
                AppUtils.goToActivationBasic(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i(LOG_TAG, "Hello here");

        switch (requestCode){
            case VARIABLES.ExtStoragePermissionsRqtCode:
                if(!ExternalStorageUtils.isPermissionGranted(this,
                        VARIABLES.rdExtStrPermission)){
                    AppUtils.showAppErrorDialog(this, "Read permission not granted");
                }
                if(!ExternalStorageUtils.isPermissionGranted(this,
                        VARIABLES.wrtExtStrPermission)){
                    AppUtils.showAppErrorDialog(this, "Write permission not granted");
                }
                break;
        }

        initApp();
    }

    @Override
    public void onSignInSuccess(CognitoUserSession userSession, CognitoDevice newDevice,
                                String userID, String password) {
        AppUtils.goToMainBasic(this);
    }

    @Override
    public void onSignInGetMFA(MultiFactorAuthenticationContinuation continuation) {

    }

    @Override
    public void onSignInAuthChallenged(ChallengeContinuation continuation) {

    }

    @Override
    public void onSignInFailure(Exception exception) {

    }

    private void initApp(){
        if(ExternalStorageUtils.isPermissionGranted(this, VARIABLES.wrtExtStrPermission)
                && ExternalStorageUtils.isPermissionGranted(
                this, VARIABLES.rdExtStrPermission)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initializeAwsMobileClient(activity);
                }
            }, SPLASH_TIME_OUT);

            if(ExternalStorageUtils.validateDysplaceLibraryStructure() == VARIABLES.FAILURE_CODE){
                AppUtils.showAppErrorDialog(this, "Failed to validate Dysplace " +
                        "Library Structure");
            }
        }
        else{
            ExternalStorageUtils.validateExtStoragePermissions(this);
        }
    }
}
