package com.dysplace.dysplaceproto1.app_intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dysplace.dysplaceproto1.R;
import com.dysplace.dysplaceproto1.utilities.AwsCogUtils;
import com.dysplace.dysplaceproto1.utilities.ConfigUtils;

public class NewPasswordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NewPasswordActivity";

    private EditText passOneView;
    private EditText passTwoView;

    private Activity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword);

        ConfigUtils.configureActivity(this);

        passOneView = findViewById(R.id.newPassOneView);
        passTwoView = findViewById(R.id.newPassTwoView);
        Button doneBtn = findViewById(R.id.newPassBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passOne = passOneView.getText().toString();
                String passTwo = passTwoView.getText().toString();

                if(!passOne.equals(passTwo)){
                    String message = "The two passwords don't match. Try again";
                    Log.i(LOG_TAG, message);
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    passOneView.setText("");
                    passTwoView.setText("");
                }
                else if(!AwsCogUtils.isPasswordValid(passOneView.getText().toString())){
                    String message = "Invalid Password. Min 8 chars. At least one uppercase," +
                            " at least one lowercase characters and at least one number";
                    Log.i(LOG_TAG, message);
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
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
