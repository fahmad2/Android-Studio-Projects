package com.dysplace.awstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
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
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AWS Test Main";

    private AWSConfiguration awsConfiguration;
    private String transferUtilStr = "S3TransferUtility";

    private Button btnSignIn;
    private Button btnSignOut;
    private Button btnListAssets;
    private Button btnDownloadVideos;

    private static final File extPath = Environment.getExternalStorageDirectory();
    private static final File extDirPath = new File(extPath.getAbsolutePath()+
            File.separator+"Dysplace"+File.separator+"Videos"+File.separator);
    private static final String fileBaseName = "dysplace_asset_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.validatePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.validatePermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        btnSignIn = findViewById(R.id.signInButton);
        btnSignOut = findViewById(R.id.signOutButton);
        btnListAssets = findViewById(R.id.listAssetsButton);
        btnDownloadVideos = findViewById(R.id.downloadButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnListAssets.setVisibility(View.INVISIBLE);
                signInUser("dys-test-user-2", "Test12345");
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnListAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAssets();
            }
        });
        btnDownloadVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s3Path = "public/test_folder1";
                String objectName = "appleAirpods.mp4";
                downloadWithTransferUtility(s3Path, objectName, extDirPath);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initializeAWSMobileClient();

        awsConfiguration = AWSMobileClient.getInstance().getConfiguration();
    }

    private void initializeAWSMobileClient(){
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.i(LOG_TAG, "AWSMobileClient is instantiated and you are connected to AWS!");
                btnListAssets.setVisibility(View.VISIBLE);
                btnDownloadVideos.setVisibility(View.VISIBLE);
                if(isUserSignedIn()){
                    String userID = IdentityManager.getDefaultIdentityManager().getCachedUserID();
                    Log.i(LOG_TAG, "User: "+userID+" was signed in already");
                    btnSignOut.setVisibility(View.VISIBLE);
                }
                else{
                    btnSignIn.setVisibility(View.VISIBLE);
                    Log.i(LOG_TAG, "User was logged out previously");
                }
            }
        }).execute();
    }

    private void signInUser(final String userId, final String password){

        CognitoUserPool cognitoUserPool = new CognitoUserPool(this, awsConfiguration);
        CognitoUser cognitoUser = cognitoUserPool.getUser(userId);

        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(LOG_TAG,"User: "+userId+" signed in successfully");
                btnListAssets.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.INVISIBLE);
                btnSignOut.setVisibility(View.VISIBLE);

                initializeAWSMobileClient();
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

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(LOG_TAG, "User: "+userId+ " signing in unsuccessful");
            }
        };

        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    private void signOut(){
        Log.i(LOG_TAG, "Attempting to sign out user");
        if(this.isUserSignedIn()){
            String userID = IdentityManager.getDefaultIdentityManager().getCachedUserID();
            Log.i(LOG_TAG, "Signing out user: "+userID);
            IdentityManager.getDefaultIdentityManager().signOut();
            btnSignOut.setVisibility(View.INVISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    private boolean isUserSignedIn(){
        return IdentityManager.getDefaultIdentityManager().isUserSignedIn();
    }

    private void listAssets(){

        if(this.isUserSignedIn()){
            Log.i(LOG_TAG, "User: "+IdentityManager.getDefaultIdentityManager().getCachedUserID()+" is signed in");
        }
        else{
            Log.i(LOG_TAG, "No user is signed in");
        }

        Log.d(LOG_TAG, "CredentialsProvider: "+AWSMobileClient.getInstance().getCredentialsProvider().toString());

        AmazonS3Client amazonS3Client =
                new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider());

        String bucketName = this.getBucketName();

        new BucketListing().execute(amazonS3Client, bucketName);
    }

    private void downloadWithTransferUtility(String s3Path, String objectName, File fileDir) {

        String s3FilePath = s3Path+File.separator+objectName;

        Log.i(LOG_TAG, "Downloading Object: "+s3FilePath);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        File filePath = new File(fileDir+File.separator+fileBaseName+objectName);

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

    private String getBucketName(){
        JSONObject jsonObject = awsConfiguration.optJsonObject(transferUtilStr);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        String wtsPermission = "android.permission.WRITE_EXTERNAL_STORAGE";

        for(int i=0; i<permissions.length; i++){
            if(permissions[i].equals(wtsPermission) && grantResults[i] == 0){
                Log.i(LOG_TAG, "Permission to write to external storage granted");
                break;
            }
        }
    }

    public void validatePermission(String permission){
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i(LOG_TAG, "Requesting Permission: "+permission);

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.i(LOG_TAG, "Permission: "+permission+" already granted");
        }
    }
}
