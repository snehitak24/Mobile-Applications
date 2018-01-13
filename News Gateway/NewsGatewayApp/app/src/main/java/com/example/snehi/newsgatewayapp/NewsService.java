/**
 * Created by snehi on 2017-04-27.
 */
package com.example.snehi.newsgatewayapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NewsService extends Service {

    private static final String TAG = "NewsService";
    private boolean running = true;
    static final String ACTION_MSG_TO_SVC = "ACTION_MSG_TO_SVC";
    ServiceReceiver receiver;
    private ArrayList<News> storyList=new ArrayList<>();
    private ArrayList<News> articleList=new ArrayList<>();
    private  NewsArticleDownloader nad;
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";

    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new ServiceReceiver();

        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SVC);
        registerReceiver(receiver, filter1);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(running){

                        if(storyList.isEmpty()){
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        else{
                            Intent intent = new Intent();
                            intent.setAction(ACTION_NEWS_STORY);
                            Log.d(TAG, "run: ");
                            intent.putParcelableArrayListExtra("storylist", storyList);
                            sendBroadcast(intent);
                            storyList.clear();
                        }
                }
            }
        }).start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        unregisterReceiver(receiver);
        running = false;

        super.onDestroy();
    }

    class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {

                case ACTION_MSG_TO_SVC:

                    String source=null;
                        if(intent.hasExtra("SOURCEOBJ")){
                        source  =  intent.getStringExtra("SOURCEOBJ");
                        Log.d(TAG, "onReceive: "+source);

                        if(source.contains("-"))
                            source.replace("-","");
                            //Toast.makeText(getApplicationContext(), "Hello "+source, Toast.LENGTH_SHORT).show();
                        nad = new NewsArticleDownloader(NewsService.this, source);
                        nad.execute();
                        }
                        else
                        {
                            Log.d(TAG, "onReceive: ");
                        }
                    break;
            }
        }
    }

    public void setArticles(ArrayList<News> list) {
        articleList.clear();
        storyList.addAll(list);
        articleList.addAll(list);
    }
}