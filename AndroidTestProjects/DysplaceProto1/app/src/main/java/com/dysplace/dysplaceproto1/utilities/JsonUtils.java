package com.dysplace.dysplaceproto1.utilities;

import android.content.Context;

import com.dysplace.dysplaceproto1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

@Deprecated
public class JsonUtils {

    /*
    private static JSONObject getJsonFromRaw(int rawID, Context context){
        InputStream is = context.getResources().openRawResource(rawID);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe){

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static JSONObject getDysConfigJson(Context context){
        return getJsonFromRaw(R.raw.dysplaceconfiguration, context);
    }

    public static String getRootFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);

        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonLibStructKey);
            JSONObject rootDirObject = libStructObject.getJSONObject(VARIABLES.jsonRootDirKey);
            return rootDirObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getVideoFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);


        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonLibStructKey);
            JSONArray subDirs = libStructObject.getJSONArray(VARIABLES.jsonSubDirKey);
            return subDirs.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getTxtFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);


        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonLibStructKey);
            JSONArray subDirs = libStructObject.getJSONArray(VARIABLES.jsonSubDirKey);
            return subDirs.get(1).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getS3CampaingFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);

        try {
            JSONObject s3StructObject = mainObject.getJSONObject(VARIABLES.jsonS3StructKey);
            JSONObject campaignObject = s3StructObject.getJSONObject(VARIABLES.jsonCampaignKey);
            return campaignObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getS3ActiveFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);


        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonS3StructKey);
            JSONArray subDirs = libStructObject.getJSONArray(VARIABLES.jsonCampaignSubKey);
            return subDirs.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getS3ArchiveFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);


        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonS3StructKey);
            JSONArray subDirs = libStructObject.getJSONArray(VARIABLES.jsonCampaignSubKey);
            return subDirs.get(1).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getS3StagingFolderName(Context context){
        JSONObject mainObject = getDysConfigJson(context);


        try {
            JSONObject libStructObject = mainObject.getJSONObject(VARIABLES.jsonS3StructKey);
            JSONArray subDirs = libStructObject.getJSONArray(VARIABLES.jsonCampaignSubKey);
            return subDirs.get(2).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    */
}
