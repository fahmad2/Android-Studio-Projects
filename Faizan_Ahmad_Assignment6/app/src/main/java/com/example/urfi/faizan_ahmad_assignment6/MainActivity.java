package com.example.urfi.faizan_ahmad_assignment6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private SampleReceiver sampleReceiver;
    static final String BROADCAST_TYPE_A = "News_Source_Broadcast";
    static final String BROADCAST_TYPE_B = "News_Article_Broadcast";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;
    private List<Map<String, String>> sourceListMap = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> articleListMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        intent.putExtra("NewsSource", true);
        intent.putExtra("Category", "all");
        startService(intent);

        sampleReceiver = new SampleReceiver();

        IntentFilter filter1 = new IntentFilter(BROADCAST_TYPE_A);
        registerReceiver(sampleReceiver, filter1);

        IntentFilter filter2 = new IntentFilter(BROADCAST_TYPE_B);
        registerReceiver(sampleReceiver, filter2);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setDrawer(){
        System.out.println("HELLO "+sourceListMap.size());
        items = new String[sourceListMap.size()];
        for (int i = 0; i < items.length; i++) {
            Map<String, String> mp = sourceListMap.get(i);
            items[i] = mp.get("name");
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, items));
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        intent.putExtra("NewsSource", true);
        intent.putExtra("Category", item.getTitle());
        startService(intent);
        return true;
    }

    private void selectItem(int position) {
        Toast.makeText(this, "Drawer List Item "+items[position]+" clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        intent.putExtra("NewsArticle", true);
        intent.putExtra("Title", sourceListMap.get(position).get("id"));
        startService(intent);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void parseSourceJSON(String s){
        sourceListMap.clear();

        try {
            JSONObject whole = new JSONObject(s);

            JSONArray sources = whole.getJSONArray("sources");

            for(int i=0; i<sources.length(); i++){
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jo = sources.getJSONObject(i);

                map.put("id", jo.get("id").toString());

                map.put("name", jo.get("name").toString());

                map.put("url", jo.get("url").toString());

                map.put("category", jo.get("category").toString());


                sourceListMap.add(map);
            }
            setDrawer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseArticleJSON(String s){
        articleListMap.clear();
        try {
            JSONObject whole = new JSONObject(s);

            JSONArray sources = whole.getJSONArray("articles");

            for(int i=0; i<sources.length(); i++){
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jo = sources.getJSONObject(i);

                map.put("author", jo.get("author").toString());

                map.put("title", jo.get("title").toString());

                map.put("description", jo.get("description").toString());

                map.put("urlToImage", jo.get("urlToImage").toString());

                map.put("publishedAt", jo.get("publishedAt").toString());


                articleListMap.add(map);
            }

            Intent i = new Intent(MainActivity.this, ArticlesActivity.class);
            Article a = new Article();
            i.putExtra("Article", (Serializable) a);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(sampleReceiver);
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    class Article {

    }


    class SampleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case BROADCAST_TYPE_A:
                    String sourceData = "";
                    if(intent.hasExtra("JSON Data")){
                        sourceData = intent.getStringExtra("JSON Data");
                        parseSourceJSON(sourceData);
                    }
                    break;
                case BROADCAST_TYPE_B:
                    String articleData = "";
                    if(intent.hasExtra("JSON Data")){
                        articleData = intent.getStringExtra("JSON Data");
                        parseSourceJSON(articleData);
                    }
                    break;
            }
        }
    }
}