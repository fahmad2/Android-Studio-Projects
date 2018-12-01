package com.dysplace.awscloudlogic;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class ActivationActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ActivationActivity";

    private TextView userIdView;
    private TextView passwordView;
    private Button loginButton;

    private static boolean newPasswordRequired = false;
    private String newPassword;

    // Continuations
    NewPasswordContinuation newPasswordContinuation;

    // Dialogs
    Dialog waitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        Utils.disableNotificationBar(this);

        userIdView = findViewById(R.id.userIdEditText);
        passwordView = findViewById(R.id.passEditText);
        loginButton = findViewById(R.id.loginButton);

        final Context context = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userIdView.getText().length() == 0 || passwordView.getText().length() == 0){
                    String toastText = "Can not leave userID or password blank";
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                }
                else if(!Utils.isPasswordValid(passwordView.getText().toString())){
                    String message = "Invalid Password. Min 8 chars. At least one uppercase," +
                            " at least one lowercase characters and at least one number";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    passwordView.setText("");
                }
                else{
                    String userID = userIdView.getText().toString();
                    String password = passwordView.getText().toString();
                    signInUser(userID, password);
                    showWaitDialog("Trying to sign you in");
                }
            }
        });
    }

    public void signInUser(final String userId, final String password){

        final Activity activity = this;

        AWSConfiguration awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        CognitoUserPool cognitoUserPool = new CognitoUserPool(activity, awsConfiguration);
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);

        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(LOG_TAG,"User: "+userId+" signed in successfully");

                if(newPasswordRequired && !newPassword.equals("")){
                    Utils.saveCredentials(userId, newPassword, activity);
                }
                else{
                    Utils.saveCredentials(userId, password, activity);
                }

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
                closeWaitDialog();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                closeWaitDialog();
                Log.i(LOG_TAG, "Authentication Challenge: "+continuation.getChallengeName());
                if("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())){
                    Log.i(LOG_TAG, "New Password is required");

                    // A new user is trying to sign in for the first time after
                    // admin has created the user’s account

                    // Cast to NewPasswordContinuation for easier access to challenge parameters
                    newPasswordContinuation = (NewPasswordContinuation) continuation;

                    showWaitDialog("Waiting for user to change password");

                    newPasswordRequired = true;

                    // Prompt user to set a new password and values for required att

                    firstTimeSignIn();
                }
            }

            @Override
            public void onFailure(Exception exception) {
                closeWaitDialog();
                String message = exception.getMessage();
                Log.i(LOG_TAG, "User: "+userId+ " signing in unsuccessful: "+message);

                String invalidUserError = "User does not exist";

                String incorrectUserPassError = "Incorrect username or password";

                if(message.contains(invalidUserError)){
                    Toast.makeText(activity, invalidUserError, Toast.LENGTH_LONG).show();
                }
                else if(message.contains(incorrectUserPassError)){
                    Toast.makeText(activity, incorrectUserPassError, Toast.LENGTH_LONG).show();
                }

                Toast.makeText(activity, "Sign in unsuccessful.", Toast.LENGTH_LONG).show();
            }
        };

        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    private void firstTimeSignIn() {
        Intent newPasswordActivity = new Intent(this, ChangePasswordActivity.class);
        startActivityForResult(newPasswordActivity, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                //New password
                closeWaitDialog();
                Boolean continueSignIn = false;
                if (resultCode == RESULT_OK) {
                    continueSignIn = data.getBooleanExtra("continueSignIn", false);
                }
                if(continueSignIn){
                    String password = data.getStringExtra("password");

                    if(password == null){
                        Log.i(LOG_TAG, "Unable to get password");
                    }
                    else{
                        newPassword = password;
                        // Set new user password
                        newPasswordContinuation.setPassword(newPassword);

                        // Allow the sign-in to complete
                        newPasswordContinuation.continueTask();
                    }
                }
                break;
        }

    }

    private void initializeAWSMobileClient(final Activity activity){

        AWSMobileClient.getInstance().initialize(activity, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.i(LOG_TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                activity.finishAffinity();

                closeWaitDialog();
                Toast.makeText(activity, "Sign in successful.", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }


}
