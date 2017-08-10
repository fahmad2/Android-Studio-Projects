package com.example.urfi.faizan_ahmad_assignment4;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by urfi on 3/20/17.
 */

public class AsyncFinancialDataLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private final String queryUrl = "http://finance.google.com/finance/info?client=ig&q=";
    private Map<String, String> mp = new HashMap<String, String>();

    public AsyncFinancialDataLoader(MainActivity ma) { this.mainActivity = ma; }

    @Override
    protected void onPostExecute(String s) {
        mainActivity.updateFinancialData(mp);
    }

    @Override
    protected String doInBackground(String... params) {

        String urlToUse = queryUrl+params[0];
        System.out.println(urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            reader.readLine();
            reader.readLine();

            sb.append('[').append('\n');
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            System.out.println(sb.toString());

        } catch (Exception e) {
            return null;
        }

        parseJSON(sb.toString());

        return null;
    }

    private void parseJSON(String s){
        try {
            JSONArray finArray = new JSONArray(s);

            JSONObject jsonObject = finArray.getJSONObject(0);

            mp.put("symbol", (String) jsonObject.get("t"));
            mp.put("price", (String) jsonObject.get("l"));
            mp.put("change", (String) jsonObject.get("c"));
            mp.put("cPercent", (String) jsonObject.get("cp"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}