package com.example.urfi.faizan_ahmad_assignment6;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by urfi on 5/6/17.
 */

public class AsyncNewsSourceDownloader extends AsyncTask<String, Void, String> {

    private NewsService newsService;
    private boolean all;
    private final String apiUrl = "https://newsapi.org/v1/sources?language=en&country=us";
    private String output = "";

    public AsyncNewsSourceDownloader(NewsService ma, boolean a) {
        this.newsService = ma;
        this.all = a;
    }

    @Override
    protected void onPostExecute(String s){
        Intent intent = new Intent();
        intent.setAction(MainActivity.BROADCAST_TYPE_A);
        intent.putExtra("JSON Data", output);
        newsService.sendBroadcast(intent);
    }

    @Override
    protected String doInBackground(String... params) {

        String urlToUse = "";

        if(all){
            urlToUse = apiUrl+"&apiKey=347c80c9dab4413e9ddaaaa7cb271038";
        }
        else {
            urlToUse = apiUrl+"&category="+params[0]+"&apiKey=347c80c9dab4413e9ddaaaa7cb271038";
        }

        System.out.println(urlToUse);

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

        } catch (Exception e) {
            return null;
        }

        output = sb.toString();

        return null;
    }


}
