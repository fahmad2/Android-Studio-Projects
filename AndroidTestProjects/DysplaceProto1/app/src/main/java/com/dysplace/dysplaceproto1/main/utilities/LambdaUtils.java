package com.dysplace.dysplaceproto1.main.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.dysplace.dysplaceproto1.main.async_tasks.LambdaInvokerTask;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids.GnaiRequest;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played.RapRequest;
import com.dysplace.dysplaceproto1.pojos.user_authorization.DysCredentials;
import com.dysplace.dysplaceproto1.utilities.AppUtils;
import com.dysplace.dysplaceproto1.utilities.AwsCogUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;

import java.util.Date;

public class LambdaUtils {

    private static final String LOG_TAG = "LambdaUtils";

    public static void invokeGetNewAssetIDs(Activity activity, LatLng latLng,
                                            ConcatenatingMediaSource concatMediaSource){

        final String methodName = "invokeGetNewAssetIDs";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        Context context = activity.getApplicationContext();

        AWSCredentialsProvider provider = AWSMobileClient.getInstance().getCredentialsProvider();

        LambdaInvokerFactory factory = LambdaInvokerFactory.builder().context(context).
                region(Regions.AP_SOUTH_1).credentialsProvider(provider).build();

        GnaiRequest gnaiRequest = new GnaiRequest(latLng);

        new LambdaInvokerTask().execute(factory, VARIABLES.newAssetIDsLambdaCode, gnaiRequest,
                concatMediaSource, activity);
    }

    public static void invokeReportAssetPlayed(Activity activity, AssetData assetData,
                                               LatLng latLng, String date){

        final String methodName = "invokeReportAssetPlayed";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        AWSCredentialsProvider provider = AWSMobileClient.getInstance().getCredentialsProvider();
        Context context = activity.getApplicationContext();

        LambdaInvokerFactory factory = LambdaInvokerFactory.builder().context(context).
                region(Regions.AP_SOUTH_1).credentialsProvider(provider).build();

        DysCredentials dysCredentials = AwsCogUtils.getCredentials(activity);

        if(dysCredentials == null){
            Log.e(METHOD_TAG, "Dysplace Credentials not available. Returning to SplashActivity");
            AppUtils.goToSplashBasic(activity);
            return;
        }

        String deviceID = dysCredentials.getUserID();

        RapRequest rapRequest = new RapRequest(assetData, latLng, date, deviceID);

        new LambdaInvokerTask().execute(factory, VARIABLES.assetPlayedLambdaCode, rapRequest,
                activity);
    }
}
