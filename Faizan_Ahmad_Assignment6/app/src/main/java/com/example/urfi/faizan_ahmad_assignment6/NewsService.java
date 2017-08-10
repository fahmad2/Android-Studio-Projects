package com.example.urfi.faizan_ahmad_assignment6;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by urfi on 5/6/17.
 */

public class NewsService extends Service {

    String str1 = "";
    String str2 = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR


        boolean bol1 = false;
        boolean bol2 = false;



        if(intent.hasExtra("NewsSource"));{
            bol1 = intent.getBooleanExtra("NewsSource", false);
            if(intent.hasExtra("Category"));{
                str1 = intent.getStringExtra("Category");
            }
        }

        if(intent.hasExtra("NewsArticle"));{
            bol2 = intent.getBooleanExtra("NewsArticle", false);
            if(intent.hasExtra("Title"));{
                str2 = intent.getStringExtra("Title");
            }
        }

        if(bol1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doAsyncSourceLoad(str1);
                    stopSelf();
                }
            }).start();
        }

        if(bol2){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doAsyncArticleLoad(str2);
                    stopSelf();
                }
            }).start();
        }
        return Service.START_NOT_STICKY;
    }

    public void doAsyncSourceLoad(String s){
        boolean flag = false;

        if(s.equalsIgnoreCase("all")){
            flag = true;
        }

        AsyncNewsSourceDownloader alt = new AsyncNewsSourceDownloader(this, flag);
        alt.execute(s);
    }

    public void doAsyncArticleLoad(String s){
        AsyncNewsArticleDownloader alt = new AsyncNewsArticleDownloader(this);
        alt.execute(s);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
