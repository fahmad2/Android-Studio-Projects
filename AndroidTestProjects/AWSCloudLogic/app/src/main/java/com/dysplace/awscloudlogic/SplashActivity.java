package com.dysplace.awscloudlogic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SplashActivity";
    private static final int SPLASH_TIME_OUT = 2000;
    AWSConfiguration awsConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Utils.disableNotificationBar(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeAWSMobileClient();
            }
        }, SPLASH_TIME_OUT);
    }

    private void signInUser(final String userId, final String password){

        awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        CognitoUserPool cognitoUserPool = new CognitoUserPool(this, awsConfiguration);
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);

        final Activity activity = this;

        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(LOG_TAG,"User: "+userId+" signed in successfully");
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                activity.finishAffinity();
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                // The API needs user sign-in credentials to continue
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);

                // Pass the user sign-in credentials to the continuation
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                // Allow the sign-in to continue
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(LOG_TAG, "Authentication Challenge: "+continuation.getChallengeName());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(LOG_TAG, "User: "+userId+ " signing in unsuccessful");

            }
        };

        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    // TODO: In main app this method should be in an interface and implement here because different activities will have different onComplete methods
    private void initializeAWSMobileClient(){
        final Activity activity = this;

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.i(LOG_TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
                if(Utils.isUserSignedIn()){
                    Log.i(LOG_TAG, "User is already signed in");

                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.i(LOG_TAG, "User not signed in");

                    if(Utils.areCredentialsAvailable(activity)){
                        Log.i(LOG_TAG, "Device has already been activated");
                        DysplaceCredentials dysplaceCredentials = Utils.getCredentials(activity);
                        if(dysplaceCredentials == null){
                            Log.i(LOG_TAG, "Failed to get credentials. Going to Sign in page");
                            Intent intent = new Intent(activity, ActivationActivity.class);
                            activity.startActivity(intent);
                            activity.finishAffinity();
                        }
                        else{
                            String userID = dysplaceCredentials.getUserID();
                            String password = dysplaceCredentials.getPassword();
                            if(userID != null && password != null){
                                Log.i(LOG_TAG, "Credentials are eligible");
                                signInUser(userID, password);
                            }
                            else{
                                Log.i(LOG_TAG, "Saved Credentials are invalid");
                                Intent intent = new Intent(activity, ActivationActivity.class);
                                activity.startActivity(intent);
                                activity.finishAffinity();
                            }
                        }
                    }
                    else{
                        Log.i(LOG_TAG, "Device activation is required");
                        Intent intent = new Intent(activity, ActivationActivity.class);
                        startActivity(intent);
                        activity.finishAffinity();
                    }
                }

            }
        }).execute();
    }
}