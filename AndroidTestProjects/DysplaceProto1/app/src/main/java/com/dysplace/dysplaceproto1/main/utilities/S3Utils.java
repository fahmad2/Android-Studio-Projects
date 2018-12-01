package com.dysplace.dysplaceproto1.main.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.dysplace.dysplaceproto1.main.async_tasks.BucketSyncTask;
import com.dysplace.dysplaceproto1.utilities.AssetPathUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class S3Utils {

    private static final String LOG_TAG = "S3Utils";

    public static void syncSets(Context context, Set<String> setS3Assets,
                                Set<String> setLocalAssets){
        List<String> assetsToDelete = new ArrayList<>();
        List<String> assetsToDownload = new ArrayList<>();

        for(String string: setS3Assets){
            if(!setLocalAssets.contains(string)){
                String assetID = string.split(VARIABLES.assetBaseName)[1];
                assetsToDownload.add(assetID);

                Log.i(LOG_TAG, "Asset to be downloaded: "+assetID);
            }
        }

        for(String string: setLocalAssets){
            if(!setS3Assets.contains(string)){
                assetsToDelete.add(string);
                Log.i(LOG_TAG, "Asset to be deleted: "+string);
            }
        }

        AssetPathUtils.deleteLocalAssets(assetsToDelete);

        downloadAssets(context, assetsToDownload);
    }

    public static void syncWithBucket(Activity activity){

        Context context = activity.getApplicationContext();

        AmazonS3Client amazonS3Client = new AmazonS3Client(
                AWSMobileClient.getInstance().getCredentialsProvider());
        AWSConfiguration awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        String bucketName = getBucketName(awsConfiguration);

        new BucketSyncTask().execute(context, amazonS3Client, bucketName, VARIABLES.prefixS3Active);
    }

    private static void downloadAssets(Context context, List<String> assetsToDownload){
        for(String assetID: assetsToDownload){
            downloadWithTransferUtility(context, VARIABLES.prefixS3Active, assetID,
                    VARIABLES.DYS_ASSET_DIRECTORY);
        }
    }

    private static void downloadWithTransferUtility(Context context, String s3Path,
                                                    String objectName, File fileDir) {

        String s3FilePath = s3Path+objectName;

        Log.i(LOG_TAG, "Downloading Object: "+s3FilePath);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        File filePath = new File(fileDir+File.separator+
                AssetPathUtils.parseAssetName(objectName));

        Log.i(LOG_TAG, "Saving file in: "+filePath.toString());

        TransferObserver downloadObserver =
                transferUtility.download(
                        s3FilePath, filePath);

        Log.i(LOG_TAG, "Downloading started");

        // Attach a listener to the observer to get notified of the
        // updates in the state and the progress
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a complete download
                    Log.i(LOG_TAG, "Download complete");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.i(LOG_TAG, "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.i(LOG_TAG, "Error occurred: "+ex.getMessage());
            }

        });
    }

    private static String getBucketName(AWSConfiguration awsConfiguration){
        JSONObject jsonObject = awsConfiguration.optJsonObject(VARIABLES.transferUtilStr);
        String bucketName = "";

        try {
            bucketName = jsonObject.get("Bucket").toString();
            Log.i(LOG_TAG, "Bucket name is: "+bucketName);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "Failed to return bucket name");
        }

        return bucketName;
    }
}
