package com.example.urfi.faizan_ahmad_assignment6;

import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urfi on 5/6/17.
 */

public class AsyncNewsArticleDownloader extends AsyncTask<String, Void, String> {

    private NewsService newsService;
    private final String apiUrl = "https://newsapi.org/v1/articles?source=";
    private String output = "";

    public AsyncNewsArticleDownloader(NewsService ma) { this.newsService = ma; }

    @Override
    protected void onPostExecute(String s){
        Intent intent = new Intent();
        intent.setAction(MainActivity.BROADCAST_TYPE_B);
        intent.putExtra("JSON Data", output);
        newsService.sendBroadcast(intent);
    }

    @Override
    protected String doInBackground(String... params) {

        String urlToUse = apiUrl+params[0].toLowerCase()+"&apiKey=347c80c9dab4413e9ddaaaa7cb271038";

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

        System.out.println(sb.toString());
        output = sb.toString();

        return null;
    }


}
