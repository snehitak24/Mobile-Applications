/**
 * Created by snehi on 2017-04-27.
 */
package com.example.snehi.newsgatewayapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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

public class NewsSourceDownloader  extends AsyncTask<String,String,String> {

        private MainActivity mainActivity;
        private int count;
        private String sourceUrl;
        private static final String TAG = "NewsSourceLoader";
        private ArrayList<News> sourceList=new ArrayList<>();
        private HashSet<String> categories= new HashSet<>();

        String category="";
        public NewsSourceDownloader(MainActivity ma, String cat) {
            mainActivity = ma;
            category=cat;
            if(cat!=null)
            if (cat.equals("all"))
                category="";
            sourceUrl="https://newsapi.org/v1/sources?language=en&country=us&category="+category+"&apiKey=069d00f68ec648c69c3bda66d6ecc23a";
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<Source> sourcelist = parseJSON(s);
            mainActivity.setSources(sourcelist,categories);
        }


        @Override
        protected String doInBackground(String... params) {
            Uri dataUri = Uri.parse(sourceUrl);
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


        private ArrayList<Source> parseJSON(String s) {
            Log.d(TAG, "parseJSON: "+s);
            ArrayList<Source> sourceList = new ArrayList<>();

            try {
                JSONObject jObject=null;
                if(s!=null)
                jObject = new JSONObject(s);
                JSONArray jsource = jObject.getJSONArray("sources");
                if(jsource!=null)
                count=jsource.length();

                if(count!=0){
                    for (int i = 0; i < count; i++) {
                        JSONObject jsources = (JSONObject) jsource.get(i);
                        String id= jsources.getString("id");
                        String name=jsources.getString("name");;
                        String url=jsources.getString("url");;
                        String category=jsources.getString("category");
                        categories.add(category);
                        sourceList.add(new Source(id,name,url,category));
                    }
                }
                return sourceList;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            catch (Exception e) {
                Log.d(TAG, "parseJSaON: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsSourceDownloader that = (NewsSourceDownloader) o;
        return category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return category.hashCode();
    }
}