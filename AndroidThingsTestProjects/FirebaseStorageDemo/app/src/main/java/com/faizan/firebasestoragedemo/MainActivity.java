package com.faizan.firebasestoragedemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button testButton;
    private final String TAG = "MainActivity";

    FirebaseAuth firebaseAuth;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference fbLogoRef;
    StorageReference fbLogoImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = findViewById(R.id.uploadButton);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        fbLogoRef = storageReference.child("facebook_logo.jpg");
        fbLogoImageRef = storageReference.child("images/facebook_logo.jpg");

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(getApplicationContext(), R.drawable.facebook_logo, fbLogoRef);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            Log.d(TAG, "User: "+user.getDisplayName()+" already signed in");
        }
        else {
            Log.d(TAG, "Trying to sign in anonymously");
            signInAnonymously();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            Log.d(TAG, "User: "+user.getDisplayName()+" is being deleted.");
            //user.delete();
        }
    }

    protected void downloadVideo(Context context, StorageReference storageRef){
    }

    protected void uploadImage(Context context, int resourceId, StorageReference storageRef){

        Log.d(TAG, "Trying to upload picture");

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG,"Upload unsuccessful with Exception: "+exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                if (downloadUrl != null) {
                    Log.d(TAG, "Upload successful with Url: " + downloadUrl.toString());
                }
                else{
                    Log.d(TAG,"Null pointer on downloadUrl");
                }
            }
        });
    }

    private void signInAnonymously() {
        firebaseAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
                Log.d(TAG,"User: "+authResult.getUser().getDisplayName()+" signed in successfully");
            }


        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "Failed to sign in anonymously with Exception: "+exception.getMessage());
                    }
                });
        firebaseAuth.signInAnonymously();
    }
}
