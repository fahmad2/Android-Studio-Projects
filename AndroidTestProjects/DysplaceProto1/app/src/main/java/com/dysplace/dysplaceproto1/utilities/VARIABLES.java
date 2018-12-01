package com.dysplace.dysplaceproto1.utilities;

import android.Manifest;
import android.os.Environment;

import java.io.File;

public class VARIABLES {

    // PERMISSION REQUEST CODES
    public static final int ExtStoragePermissionsRqtCode = 0;

    // LAMBDA INVOCATION REQUEST CODES
    public static final int defaultLambdaCode = 0;
    public static final int newAssetIDsLambdaCode = 1;
    public static final int assetPlayedLambdaCode = 2;

    // PRIVATE STRINGS
    private static final String s3CampaignsDirName = "campaigns";
    private static final String prefixS3Campaigns = s3CampaignsDirName+File.separator;
    private static final String s3ActiveDirName = "active-level";
    private static final String localLibDysFolderName = "DYSPLACE";
    private static final String localLibAssetFolderName = "ASSETS";
    private static final String localLibTxtFolderName = "TEXT_FILES";

    // PUBLIC STRINGS
    public static final String rdExtStrPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String wrtExtStrPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String mp4Extension = "mp4";
    public static final String dysplaceDefaultAsset1 = "dysplace_asset1";
    public static final String assetBaseName = "dysplace_asset_";
    public static final String prefixS3Active = prefixS3Campaigns+s3ActiveDirName+File.separator;
    public static final String transferUtilStr = "S3TransferUtility";
    public static final String SETTING_INFOS = "SETTING_Infos";
    public static final String userIdKey = "DYSPLACE_USER_ID";
    public static final String passwordKey = "DYSPLACE_PASSWORD";
    public static final String jsonAssetIDsKey = "arrayAssetIDs";
    public static final String pakistanStandardTimeZone = "GMT+05:00";

    // FLOAT VALUES
    public static final float muteVolume = 0f;
    public static final float unMuteVolume = 1f;

    // RESULT CODES
    public static final int SUCCESS_CODE = 0;
    public static final int FAILURE_CODE = 1;

    // Files
    private static final File EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory();
    public static final File DYSPLACE_DIRECTORY =
            new File(EXTERNAL_STORAGE_DIRECTORY, localLibDysFolderName);
    public static final File DYS_ASSET_DIRECTORY =
            new File(DYSPLACE_DIRECTORY, localLibAssetFolderName);
    public static final File DYS_TXT_DIRECTORY =
            new File(DYSPLACE_DIRECTORY, localLibTxtFolderName);
}
