package com.dysplace.dysplaceproto1.interfaces.user_authorization;

import android.app.Activity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
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

public interface AwsCogActivity {

    String LOG_TAG = "AwsCogActivity";

    default void signInUser(final String userId, final String password, final Activity activity){

        AWSConfiguration awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        CognitoUserPool cognitoUserPool = new CognitoUserPool(activity, awsConfiguration);
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);

        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(LOG_TAG,"User: "+userId+" signed in successfully");
                onSignInSuccess(userSession, newDevice, userId, password);
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
                Log.i(LOG_TAG,"MFA Code required");
                onSignInGetMFA(continuation);
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(LOG_TAG, "Authentication Challenge: "+continuation.getChallengeName());
                onSignInAuthChallenged(continuation);
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(LOG_TAG, "User: "+userId+ " signing in unsuccessful");
                onSignInFailure(exception);
            }
        };

        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    void onSignInSuccess(CognitoUserSession userSession, CognitoDevice newDevice,
                         String userID, String password);

    void onSignInGetMFA(MultiFactorAuthenticationContinuation continuation);

    void onSignInAuthChallenged(ChallengeContinuation continuation);

    void onSignInFailure(Exception exception);

}
