package com.dysplace.dysplaceproto1.interfaces.aws_mobile_client;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

public interface AwsMobHubActivity {

    String LOG_TAG = "AwsMobHubActivity";

    default void initializeAwsMobileClient(Activity activity) {
        try {
            AWSMobileClient.getInstance().initialize(activity, new AWSStartupHandler() {
                @Override
                public void onComplete(AWSStartupResult awsStartupResult) {
                    Log.i(LOG_TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
                    onConnectToAWS();
                }
            }).execute();
        }
        catch (RuntimeException re){
            Log.e(LOG_TAG, "Failed to Initialize AWSMobileClient: "+re.getMessage());
            activity.finishAndRemoveTask();
        }
    }

    void onConnectToAWS();

}
