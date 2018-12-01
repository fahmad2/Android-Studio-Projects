package com.dysplace.dysplaceproto1.main.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dysplace.dysplaceproto1.main.utilities.S3Utils;
import com.dysplace.dysplaceproto1.utilities.AssetPathUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BucketSyncTask extends AsyncTask<Object, Void, Object[]> {

    private static final String LOG_TAG = "BucketListing";

    @Override
    protected Object [] doInBackground(Object[] objects) {

        final String methodName = "doInBackground";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        String errorConsequence = "Returning. Cause: ";

        Set<String> setS3Assets = new HashSet<>();

        Context context;
        AmazonS3Client amazonS3Client;
        String bucketName;
        String dirPath;

        try {
            context = (Context) objects[0];
            amazonS3Client = (AmazonS3Client) objects[1];
            bucketName = (String) objects[2];
            dirPath = (String) objects[3];
        }
        catch (ClassCastException cce){
            String cause = cce.getMessage();
            Log.i(METHOD_TAG, errorConsequence+cause);
            return null;
        }

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest().withBucketName(bucketName).withPrefix(dirPath).withDelimiter("/");

        Log.i(METHOD_TAG, "Listing objects under bucket: "+bucketName+
                " and directory: "+dirPath);

        ObjectListing objectListing;

        try {
            objectListing = amazonS3Client.listObjects(listObjectsRequest);
        }
        catch (AmazonServiceException  ase){
            String cause = ase.getMessage();
            Log.i(METHOD_TAG, errorConsequence+cause);
            return null;
        }

        List<S3ObjectSummary> summaryList = objectListing.getObjectSummaries();
        List<String> prefixList = objectListing.getCommonPrefixes();

        Log.i(METHOD_TAG, "Total number of objects: "+summaryList.size());

        for(S3ObjectSummary summary: summaryList){
            String summaryKey = summary.getKey();
            Log.i(METHOD_TAG, "Object is: "+summaryKey);

            String [] assetNameArr = summaryKey.split(VARIABLES.prefixS3Active);

            if(assetNameArr.length == 0){
                continue;
            }

            try {
                String assetName = AssetPathUtils.parseAssetName(assetNameArr[1]);
                setS3Assets.add(assetName);
            }
            catch (ArrayIndexOutOfBoundsException aiobe){
                Log.i(METHOD_TAG, "Unable to get assetName for: "+summaryKey);
            }


        }

//        Log.i(LOG_TAG, "Total number of folders: "+prefixList.size());
//
//        for(String s: prefixList){
//            Log.i(LOG_TAG, "Folder is: "+s);
//        }

        return new Object[]{context, setS3Assets};
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Object[] results) {
        super.onPostExecute(results);

        final String methodName = "onPostExecute";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        String errorConsequence = "Returning. Cause: ";

        if(results == null){
            String cause = "Invalid param received, Failed to sync with S3 Bucket.";
            Log.i(METHOD_TAG, errorConsequence+cause);
            return;
        }

        Context context;
        Set<String> setS3Assets;

        try {
            context = (Context) results[0];
            setS3Assets = (Set<String>) results[1];
        }
        catch (ClassCastException cce){
            String cause = cce.getMessage();
            Log.i(METHOD_TAG, errorConsequence+cause);
            return;
        }

        if(context == null){
            String cause = "The context is NULL. Failed to Sync with S3 Bucket";
            Log.i(METHOD_TAG, errorConsequence+cause);
            return;
        }

        S3Utils.syncSets(context, setS3Assets, AssetPathUtils.getLocalAssetSet());
    }
}
