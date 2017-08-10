package com.example.urfi.quicknotes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static EditText et;
    private static TextView tv;
    private static Note nt;
    private FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText5);
        tv = (TextView) findViewById(R.id.textView6);

        nt = loadFile();

        if(nt != null){
            tv.setText(nt.getDate());
        }

        et.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onPause(){
        super.onPause();

        DateFormat df = new SimpleDateFormat("EEE MMM dd',' hh:mm a");
        Date dateobj = new Date();
        tv.setText("Last Update: "+df.format(dateobj));

        nt.setDate(tv.getText().toString());
        nt.setNoteDesc(et.getText().toString());

        saveNote();
    }

    private void saveNote(){
        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            fos = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            if(nt.getNoteDesc().length() == 0){
                writer.name("date").value("Not Saved");
            }
            else {
                writer.name("date").value(nt.getDate());
            }
            writer.name("description").value(nt.getNoteDesc());
            writer.endObject();
            writer.close();

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        nt = loadFile();

        if(nt != null){
            et.setText(nt.getDate());
            et.setText(nt.getNoteDesc());
        }
    }

    private Note loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        nt = new Note();
        try {
            InputStream is = getApplicationContext().openFileInput("Note.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("description")) {
                    nt.setNoteDesc(reader.nextString());
                }
                else if(name.equals("date")){
                    nt.setDate(reader.nextString());
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "No Notes present", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return nt;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", et.getText().toString());
        outState.putString("Date", tv.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        et.setText(savedInstanceState.getString("HISTORY"));
        tv.setText(savedInstanceState.getString("Date"));
    }
}
