package com.dysplace.awscloudlogic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ChangePasswordActivity";
    private EditText passOneView;
    private EditText passTwoView;
    private Button done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Utils.disableNotificationBar(this);

        passOneView = findViewById(R.id.passwordOne);
        passTwoView = findViewById(R.id.passwordTwo);
        done = findViewById(R.id.changeDone);

        final Context context = this;

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passOne = passOneView.getText().toString();
                String passTwo = passTwoView.getText().toString();

                if(!passOne.equals(passTwo)){
                    String message = "The two passwords don't match. Try again";
                    Log.i(LOG_TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    passOneView.setText("");
                    passTwoView.setText("");
                }
                else if(!Utils.isPasswordValid(passOneView.getText().toString())){
                    String message = "Invalid Password. Min 8 chars. At least one uppercase," +
                            " at least one lowercase characters and at least one number";
                    Log.i(LOG_TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    passOneView.setText("");
                    passTwoView.setText("");
                }
                else{
                    exit(true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit(false);
    }

    private void exit(Boolean continueWithSignIn) {
        Intent intent = new Intent();
        intent.putExtra("password", passOneView.getText().toString());
        intent.putExtra("continueSignIn", continueWithSignIn);
        setResult(RESULT_OK, intent);
        finish();
    }
}
