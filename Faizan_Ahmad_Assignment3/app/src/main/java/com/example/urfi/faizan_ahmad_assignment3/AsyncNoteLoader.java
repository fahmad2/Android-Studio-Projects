package com.example.urfi.faizan_ahmad_assignment3;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by urfi on 3/1/17.
 */

public class AsyncNoteLoader extends AsyncTask<String, Integer, String> {

    private MainActivity mainAct;
    private int count;

    public AsyncNoteLoader(MainActivity ma) {
        this.mainAct = ma;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainAct, "Loading Note Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        List<Note> list = parseJSON(s);
        if(list != null){
            mainAct.updateData(list);
        }
    }

    private List<Note> parseJSON(String s) {
        List<Note> noteList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jNote = (JSONObject) jObjMain.get(i);
                String title = jNote.getString("title");
                String desc = jNote.getString("description");
                String date = jNote.getString("date");

                noteList.add(new Note(title, date, desc));

            }
            return noteList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String doInBackground(String... params) {

        Note nt;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = mainAct.getApplicationContext().openFileInput("Note.json");
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        }
        catch (FileNotFoundException e) {
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
