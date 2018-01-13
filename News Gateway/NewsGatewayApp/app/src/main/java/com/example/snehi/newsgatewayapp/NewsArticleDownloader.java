package com.example.snehi.newsgatewayapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by snehi on 2017-04-27.
 */

public class NewsArticleDownloader  extends AsyncTask<String,String,String> {

    private NewsService newsService= new NewsService();
    private int count;
    String source;
    private String articleUrl;
    private static final String TAG = "NewsArticleDownloader";

    public NewsArticleDownloader(NewsService n, String source) {
        newsService = n;
        this.source=source;
        articleUrl="https://newsapi.org/v1/articles?source="+source+"&apiKey=069d00f68ec648c69c3bda66d6ecc23a";
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<News> articlelist = parseJSON(s);
        Log.d(TAG, "onPostExecute: "+articlelist.size());
        newsService.setArticles(articlelist);

    }

    @Override
    protected String doInBackground(String... params) {
        //Toast.makeText(,"do in background",Toast.LENGTH_SHORT).show();
        Uri dataUri = Uri.parse(articleUrl);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

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

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }

    private ArrayList<News> parseJSON(String s) {
        Log.d(TAG, "parseJSON: "+s);
        ArrayList<News> articleList = new ArrayList<>();

        try {
            JSONObject jObject = new JSONObject(s);
            JSONArray jarticles = jObject.getJSONArray("articles");
            Log.d(TAG, "parseJSON: size"+jarticles.length());
            count=jarticles.length();

            if(count!=0){
                for (int i = 0; i < jarticles.length(); i++) {
                    JSONObject jsources = (JSONObject) jarticles.get(i);
                    String author= jsources.getString("author");
                    Log.d(TAG, "parseJSON: titlr"+author);
                    String title=jsources.getString("title");;
                    String url=jsources.getString("urlToImage");;
                    String date=jsources.getString("publishedAt");
                    String description=jsources.getString("description");
                    String extraURL = jsources.getString("url");


                    articleList.add(new News(author,title,description,url,date,extraURL));
                }
            }
            return articleList;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        catch (Exception e) {
            Log.d(TAG, "parseJSaON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}