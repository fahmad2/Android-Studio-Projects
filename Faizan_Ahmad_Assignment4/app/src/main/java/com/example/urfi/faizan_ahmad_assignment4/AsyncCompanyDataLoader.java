package com.example.urfi.faizan_ahmad_assignment4;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urfi on 3/19/17.
 */

public class AsyncCompanyDataLoader extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncCompanyDataLoader";
    private MainActivity mainActivity;
    private final String stockUrl = "http://stocksearchapi.com/api/?api_key=ffcca727cabb204bcf33aeed2343b51f3324547f&search_text=";
    private List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

    public AsyncCompanyDataLoader(MainActivity ma) { this.mainActivity = ma; }

    @Override
    protected void onPostExecute(String s){ mainActivity.updateNameData(listMap); }

    @Override
    protected String doInBackground(String... params) { // 0 = stock symbol

        String urlToUse = stockUrl+params[0];
        Log.d(TAG, urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        parseJSON(sb.toString());

        return null;
    }

    private void parseJSON(String s) {
        try {
            JSONArray stocks = new JSONArray(s);

            for (int i = 0; i < stocks.length(); i++) {
                Map<String, String> sMap = new HashMap<String, String>();
                JSONObject jo = stocks.getJSONObject(i);
                sMap.put("company_name", (String) jo.get("company_name"));
                sMap.put("company_symbol", (String) jo.get("company_symbol"));

                listMap.add(sMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}