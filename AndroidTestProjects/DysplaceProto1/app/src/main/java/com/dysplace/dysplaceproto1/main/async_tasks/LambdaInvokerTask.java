package com.dysplace.dysplaceproto1.main.async_tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.dysplace.dysplaceproto1.interfaces.lambda_event_generator.LambdaInvokerInterface;
import com.dysplace.dysplaceproto1.main.utilities.MediaPlayerUtils;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids.GnaiRequest;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids.GnaiResponse;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played.RapRequest;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played.RapResponse;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;

import java.util.List;

public class LambdaInvokerTask extends AsyncTask<Object, Void, Object> {

    private static final String LOG_TAG = "LambdaInvokerTask";

    private static int invokeCode;
    private static ConcatenatingMediaSource concatenatingMediaSource;
    private Object activityObject;

    @Override
    protected Object doInBackground(Object... params) {

        final String methodName = "doInBackground";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        try {

            LambdaInvokerFactory factory = (LambdaInvokerFactory) params[0];

            invokeCode = (Integer) params[1];

            final LambdaInvokerInterface lambdaInvoker = factory.build(LambdaInvokerInterface.class);

            switch (invokeCode){
                case VARIABLES.defaultLambdaCode:
                    return null;
                case VARIABLES.newAssetIDsLambdaCode:
                    GnaiRequest gnaiRequest = (GnaiRequest) params[2];
                    concatenatingMediaSource = (ConcatenatingMediaSource) params[3];
                    activityObject = params[4];
                    Log.i(METHOD_TAG, "Invoking GetNewAssetIDs Lambda with request: "+
                            gnaiRequest.toString());
                    return lambdaInvoker.invokeGNAI(gnaiRequest);
                case VARIABLES.assetPlayedLambdaCode:
                    RapRequest rapRequest = (RapRequest) params[2];
                    Log.i(METHOD_TAG, "Invoking ReportAssetPlayed Lambda with request: "+
                            rapRequest.toString());
                    return lambdaInvoker.invokeRAP(rapRequest);
                default:
                    return null;
            }

        }
        catch (LambdaFunctionException | ClassCastException | AmazonServiceException |
                ArrayIndexOutOfBoundsException | NullPointerException ex) {
            Log.e(METHOD_TAG, ex.getMessage()+", "+ex.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);

        final String methodName = "onPostExecute";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        switch (invokeCode){
            case VARIABLES.defaultLambdaCode:
                return;
            case VARIABLES.newAssetIDsLambdaCode:
                try {
                    GnaiResponse gnaiResponse = (GnaiResponse) response;

                    Log.i(METHOD_TAG, "Response received: "+gnaiResponse.toString());

                    List<AssetData> listAssetData = gnaiResponse.getListAssetData();
                    Activity activity = (Activity) activityObject;
                    MediaPlayerUtils.addAssetsToQueue(activity, concatenatingMediaSource,
                            listAssetData);
                } catch (NullPointerException npe) {
                    Log.e(METHOD_TAG, "Failed to get List of Asset IDs: "+npe.getMessage());
                }
                return;
            case VARIABLES.assetPlayedLambdaCode:
                try{
                    RapResponse rapResponse = (RapResponse) response;
                    Log.i(METHOD_TAG, "Response received: "+rapResponse.toString());
                }
                catch (NullPointerException npe){
                    Log.e(METHOD_TAG, "Failed to report Asset is played: "+npe.getMessage());
                }
                break;
        }

    }

}
