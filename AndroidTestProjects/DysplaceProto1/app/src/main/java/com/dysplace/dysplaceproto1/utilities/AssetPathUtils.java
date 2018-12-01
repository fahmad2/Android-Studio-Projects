package com.dysplace.dysplaceproto1.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;

import com.dysplace.dysplaceproto1.R;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AssetPathUtils {

    private static final String LOG_TAG = "AssetPathUtils";

    public static MediaSource getDefaultMediaSource(Context context){
        /**
         *
         */

        MediaSource defaultMediaSource = null;
        Field[] idFields = R.raw.class.getFields();

        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        for(Field filename: idFields) {

            Log.i(LOG_TAG, "Current File is: "+filename.getName());

            if(!filename.getName().equalsIgnoreCase(VARIABLES.dysplaceDefaultAsset1)){
                Log.i(LOG_TAG, "Asset not dysplaceDefault. Skipping!");
                continue;
            }

            int resourcesIdentifier = resources.getIdentifier(filename.getName(), "raw", packageName);

            if(!isRawAssetMp4(resources, resourcesIdentifier)){
                Log.i(LOG_TAG, "Skipping the file: "+filename.getName()+" as it is not MP4 format");
                continue;
            }
            else{
                Log.i(LOG_TAG, "The video format for: "+filename.getName()+" is MP4");
            }

            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(resourcesIdentifier));
            final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);

            try {
                rawResourceDataSource.open(dataSpec);
            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }
            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };

            defaultMediaSource = new ExtractorMediaSource.Factory(factory).
                    createMediaSource(rawResourceDataSource.getUri());

            break;
        }


        return defaultMediaSource;
    }

    public static MediaSource getSDMediaSourceWithCode(@NonNull String assetID){

        /**
         *
         */

        Log.i(LOG_TAG, "Retrieving MediaSource for assetID: "+assetID);

        String assetLocalName = parseAssetName(assetID);

        return getSDMediaSource(assetLocalName);
    }

    private static MediaSource getSDMediaSource(@NonNull String assetLocalName){
        /**
         *
         */

        File assetFile = new File(VARIABLES.DYS_ASSET_DIRECTORY, assetLocalName);

        if(!assetFile.isFile()){
            Log.i(LOG_TAG, "VideoAsset with name: "+assetLocalName+" in dir: "+
                    VARIABLES.DYS_ASSET_DIRECTORY+" does not exist. Returning null MediaSource");

            return null;
        }


        Uri assetUri = Uri.fromFile(assetFile);

        DataSpec dataSpec = new DataSpec(assetUri);
        final FileDataSource fileDataSource = new FileDataSource();

        try{
            fileDataSource.open(dataSpec);
        }
        catch (FileDataSource.FileDataSourceException fdse){
            Log.i(LOG_TAG, "FileDataSourceException thrown");
            fdse.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };

        return new ExtractorMediaSource.Factory(factory).
                createMediaSource(fileDataSource.getUri());
    }

    private static boolean doesDysplaceAssetExist(String fullFileName){
        File videoAssetFile = new File(VARIABLES.DYS_ASSET_DIRECTORY, fullFileName);

        return videoAssetFile.isFile();
    }

    private static boolean isRawAssetMp4(Resources resources, int rawId){
        /**
         *
         */

        TypedValue value = new TypedValue();
        resources.getValue(rawId, value, true);
        String [] fullFilepath = value.string.toString().split("/");
        String fullFilename = fullFilepath[fullFilepath.length -1];
        String [] fullFilnameArr = fullFilename.split("\\.");
        String extension = fullFilnameArr[fullFilnameArr.length-1];

        return extension.equalsIgnoreCase(VARIABLES.mp4Extension);
    }

    public static Set<String> getLocalAssetSet(){

        return new HashSet<>(Arrays.asList(VARIABLES.DYS_ASSET_DIRECTORY.list()));
    }

    private static boolean deleteLocalAsset(String string) {

        File asset = new File(VARIABLES.DYS_ASSET_DIRECTORY, string);

        return !asset.exists() || asset.delete();
    }

    public static void deleteLocalAssets(List<String> stringList){
        for(String string: stringList){
            boolean bool = deleteLocalAsset(string);
            if(!bool){
                Log.i(LOG_TAG, "Failed to delete Asset: "+string);
            }
            else{
                Log.i(LOG_TAG, "Successfully deleted Asset: "+string);
            }
        }
    }

    public static String parseAssetName(String assetID){
        return VARIABLES.assetBaseName+assetID+"."+VARIABLES.mp4Extension;
    }
}
