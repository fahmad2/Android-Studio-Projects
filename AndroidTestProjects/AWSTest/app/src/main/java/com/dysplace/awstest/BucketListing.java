package com.dysplace.awstest;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

public class BucketListing extends AsyncTask {

    private static final String LOG_TAG = "BucketListing";
    private static final String mp4Ext = "mp4";

    @Override
    protected Object doInBackground(Object[] objects) {

        List<String> assetList = new ArrayList<>();

        AmazonS3Client amazonS3Client = (AmazonS3Client) objects[0];
        String bucketName = (String) objects[1];
        String folderName = "public";
        String myBucketName = "dysplacetest-ap-south-1-932166377763";
        String myFolder = "Lahore";
        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName+"/").withDelimiter("/");

        Log.i(LOG_TAG, "Listing objects under bucket: "+myBucketName);

        ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest);

        List<S3ObjectSummary> summaryList = objectListing.getObjectSummaries();
        List<String> prefixList = objectListing.getCommonPrefixes();

        Log.i(LOG_TAG, "Total number of objects: "+summaryList.size());

        for(S3ObjectSummary summary: summaryList){
            String assetName = summary.getKey();
            Log.i(LOG_TAG, "Object is: "+assetName);

            String [] assetNameArr = assetName.split(".");

            assetList.add(assetName);
        }

        Log.i(LOG_TAG, "Total number of folders: "+prefixList.size());

        for(String s: prefixList){
            Log.i(LOG_TAG, "Folder is: "+s);
        }

        return null;
    }

    protected void sendAssetNames(List<String> assetList){

    }
}
