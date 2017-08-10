package com.example.urfi.know_your_government;

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
 * Created by urfi on 4/18/17.
 */

public class AsyncOfficialDataLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private final String apiUrl = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyD1WJG_wa0QUX1_Hva4shbclMVEGHfWf7g&address=";
    private List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

    public AsyncOfficialDataLoader(MainActivity ma) { this.mainActivity = ma; }

    @Override
    protected void onPostExecute(String s){ mainActivity.updateOffList(listMap); }

    @Override
    protected String doInBackground(String... params) {

        String urlToUse = apiUrl+params[0];

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

        //System.out.println(sb.toString());
        parseJSON(sb.toString());

        return null;
    }

    private void parseJSON(String s){
        try {
            JSONObject whole = new JSONObject(s);

            JSONArray offices = whole.getJSONArray("offices");
            JSONArray officials = whole.getJSONArray("officials");
            int x = 0;

            for(int i=0; i<offices.length(); i++){
                JSONObject jo = offices.getJSONObject(i);
                String name = jo.get("name").toString();
                JSONArray temp = jo.getJSONArray("officialIndices");

                for(int j=0; j<temp.length(); j++){
                    Map<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = officials.getJSONObject(x);

                    map.put("office", name);
                    map.put("name", jsonObject.get("name").toString());

                    if(jsonObject.has("address")){
                        JSONArray ja = jsonObject.getJSONArray("address");
                        if(ja.length() > 0) {
                            JSONObject jab = ja.getJSONObject(0);

                            if(jab.has("line1")){
                                map.put("line1", jab.get("line1").toString());
                            }

                            if(jab.has("line2")){
                                map.put("line2", jab.get("line2").toString());
                            }

                            if(jab.has("city")){
                                map.put("city", jab.get("city").toString());
                            }

                            if(jab.has("state")){
                                map.put("state", jab.get("state").toString());
                            }

                            if(jab.has("zip")){
                                map.put("zip", jab.get("zip").toString());
                            }
                        }
                    }

                    if(jsonObject.has("party")){
                        map.put("party", jsonObject.get("party").toString());
                    }

                    if(jsonObject.has("phones") && jsonObject.getJSONArray("phones").length() > 0) {
                        String jap = jsonObject.getJSONArray("phones").get(0).toString();
                        map.put("phone", jap);
                    }

                    if(jsonObject.has("urls") && jsonObject.getJSONArray("urls").length() > 0) {
                        String jau = jsonObject.getJSONArray("urls").get(0).toString();
                        map.put("website", jau);
                    }

                    if(jsonObject.has("emails") && jsonObject.getJSONArray("emails").length() > 0) {
                        String jae = jsonObject.getJSONArray("emails").get(0).toString();
                        map.put("email", jae);
                    }

                    if(jsonObject.has("photoUrl")){
                        map.put("photo", jsonObject.get("photoUrl").toString());
                    }

                    if(jsonObject.has("channels")){
                        JSONArray jac = jsonObject.getJSONArray("channels");

                        for(int k=0; k<jac.length(); k++){
                            JSONObject tp = jac.getJSONObject(k);
                            map.put(tp.get("type").toString(), tp.get("id").toString());
                        }
                    }

                    listMap.add(map);
                    x++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
