package com.example.urfi.tempconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioButton celBtn;
    private EditText temp;
    private TextView result;
    private TextView allRes;
    private List<String> al = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = (EditText) findViewById(R.id.tempInput);
        result = (TextView) findViewById(R.id.tempOutput);
        celBtn = (RadioButton) findViewById(R.id.radioButton);
        allRes = (TextView) findViewById(R.id.allResults);
        allRes.setMovementMethod(new ScrollingMovementMethod());
    }

    public void calculate(View v){
        double res = 0.0;
        String str = "";
        String rstr = "";
        if(temp.getText().length() != 0){
            double tm = Double.parseDouble(temp.getText().toString());
            if(celBtn.isChecked()){
                res = cToF(tm);
                rstr = String.format("%,.1f", res);
                str = "C to F: "+tm+" -> "+rstr;
                al.add(str);
            }
            else{
                res = fToC(tm);
                rstr = String.format("%,.1f", res);
                str = "F to C: "+tm+" -> "+rstr;
                al.add(str);
            }
            result.setText(String.format("%,.1f", res));
            allRes.append(str+"\n");
        }
    }

    protected static double cToF(double c){
        double f = (c*(9.0/5.0))+32.0;
        return f;
    }

    protected static double fToC(double f){
        double c = (f-32.0)*(5.0/9.0);
        return c;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", allRes.getText().toString());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        allRes.setText(savedInstanceState.getString("HISTORY"));
    }
}
