package com.dysplace.awscloudlogic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
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

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private Button signOutButton;
    private Button signInButton;
    private Button clearCredButton;
    private Button getDataButton;
    private Button sendDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.disableNotificationBar(this);

        signOutButton = findViewById(R.id.signOutButton);
        signInButton = findViewById(R.id.signInButton);
        clearCredButton = findViewById(R.id.clearCredButton);
        getDataButton = findViewById(R.id.getDataButton);
        sendDataButton = findViewById(R.id.sendDataButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializeAWSMobileClient(this);
    }

    private void signOut(){
        Log.i(LOG_TAG, "Attempting to sign out user");
        if(Utils.isUserSignedIn()){
            String userID = IdentityManager.getDefaultIdentityManager().getCachedUserID();
            Log.i(LOG_TAG, "Signing out user: "+userID);
            IdentityManager.getDefaultIdentityManager().signOut();
        }
    }

    public void signInWithCreds(final String userId, final String password){

        final Activity activity = this;

        AWSConfiguration awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        CognitoUserPool cognitoUserPool = new CognitoUserPool(activity, awsConfiguration);
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);

        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(LOG_TAG,"User: "+userId+" signed in successfully");
                initializeAWSMobileClient(activity);
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

    private void initializeAWSMobileClient(Activity activity){

        AWSMobileClient.getInstance().initialize(activity, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.i(LOG_TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
                init();
            }
        }).execute();
    }

    private void init(){

        if(Utils.isUserSignedIn() && signOutButton.getVisibility() != View.VISIBLE){
            signOutButton.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
        }
        else if(!Utils.isUserSignedIn() && signInButton.getVisibility() != View.VISIBLE){
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.INVISIBLE);
        }

        final Activity activity = this;

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                signOutButton.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            signInWithCreds(userID, password);
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
        });

        clearCredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearCredentials(activity);
            }
        });

        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void getData(){
        Toast.makeText(this, "Get Data Button Pressed", Toast.LENGTH_LONG).show();
    }

    private void sendData(){
        Toast.makeText(this, "Send Data Button Pressed", Toast.LENGTH_LONG).show();
    }
}
