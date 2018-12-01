package com.dysplace.dysplaceproto1.app_intro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.dysplace.dysplaceproto1.R;
import com.dysplace.dysplaceproto1.interfaces.user_authorization.AwsCogActivity;
import com.dysplace.dysplaceproto1.interfaces.aws_mobile_client.AwsMobHubActivity;
import com.dysplace.dysplaceproto1.utilities.AppUtils;
import com.dysplace.dysplaceproto1.utilities.AwsCogUtils;
import com.dysplace.dysplaceproto1.utilities.ConfigUtils;
import com.dysplace.dysplaceproto1.utilities.ExternalStorageUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;

public class ActivationActivity extends AppCompatActivity implements AwsCogActivity, AwsMobHubActivity{

    private static final String LOG_TAG = "ActivationActivity";

    private TextView userIdView;
    private TextView passwordView;

    private Activity activity = this;

    private static boolean newPasswordRequired = false;
    private String newPassword;
    private final int newPassRequestCode = 1;

    // Continuations
    NewPasswordContinuation newPasswordContinuation;

    // Dialogs
    Dialog waitDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        ConfigUtils.configureActivity(this);

        userIdView = findViewById(R.id.actUserIdView);
        passwordView = findViewById(R.id.actPasswordView);
        Button loginButton = findViewById(R.id.actLoginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userIdView.getText().length() == 0 || passwordView.getText().length() == 0){
                    String toastText = "Can not leave userID or password blank";
                    Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show();
                }
                else if(!AwsCogUtils.isPasswordValid(passwordView.getText().toString())){
                    String message = "Invalid Password. Min 8 chars. At least one uppercase," +
                            " at least one lowercase characters and at least one number";
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    passwordView.setText("");
                }
                else{
                    String userID = userIdView.getText().toString();
                    String password = passwordView.getText().toString();
                    signInUser(userID, password, activity);
                    waitDialog = AppUtils.showWaitDialog(
                            "Trying to sign you in", waitDialog, activity);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case newPassRequestCode:
                //New password
                AppUtils.closeWaitDialog(waitDialog);
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

    @Override
    public void onSignInSuccess(CognitoUserSession userSession, CognitoDevice newDevice,
                                String userID, String password) {
        AppUtils.closeWaitDialog(waitDialog);
        if(newPasswordRequired && !newPassword.equals("")){
            AwsCogUtils.saveCredentials(userID, newPassword, activity);
        }
        else{
            AwsCogUtils.saveCredentials(userID, password, activity);
        }

        initializeAwsMobileClient(this);
    }

    @Override
    public void onSignInGetMFA(MultiFactorAuthenticationContinuation continuation) {
        Log.i(LOG_TAG, "onSignInGetMFA invoked");
    }

    @Override
    public void onSignInAuthChallenged(ChallengeContinuation continuation) {
        Log.i(LOG_TAG, "onSignInAuthChallenged invoked");
        AppUtils.closeWaitDialog(waitDialog);
        if("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())){
            Log.i(LOG_TAG, "New Password is required");

            // A new user is trying to sign in for the first time after
            // admin has created the user’s account

            // Cast to NewPasswordContinuation for easier access to challenge parameters
            newPasswordContinuation = (NewPasswordContinuation) continuation;

            waitDialog = AppUtils.showWaitDialog("Waiting for user to change password",
                    waitDialog, activity);

            newPasswordRequired = true;

            // Prompt user to set a new password and values for required attributes

            firstTimeSignIn();
        }
    }

    @Override
    public void onSignInFailure(Exception exception) {
        Log.i(LOG_TAG, "onSignInFailure invoked");
        AppUtils.closeWaitDialog(waitDialog);
        String message = exception.getMessage();

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

    private void firstTimeSignIn() {
        Intent newPasswordActivity = new Intent(this, NewPasswordActivity.class);
        startActivityForResult(newPasswordActivity, newPassRequestCode);
    }

    @Override
    public void onConnectToAWS() {
        AppUtils.closeWaitDialog(waitDialog);
        Toast.makeText(activity, "Sign in successful.", Toast.LENGTH_LONG).show();

        int result = ExternalStorageUtils.validateDysplaceLibraryStructure();

        if(result == VARIABLES.SUCCESS_CODE){
            AppUtils.goToMainBasic(this);
        }
        else if(result == VARIABLES.FAILURE_CODE){
            //AppUtils.showAppErrorDialog(this, "Unable to validate " + "Dysplace Library Structure");
        }


    }
}