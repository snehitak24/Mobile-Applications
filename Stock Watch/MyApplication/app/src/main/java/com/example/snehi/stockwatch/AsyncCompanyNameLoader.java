package com.example.snehi.stockwatch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by snehi on 2017-03-15.
 */

public class AsyncCompanyNameLoader extends AsyncTask<String,Integer,String> {
    //http://stocksearchapi.com/api/?api_key=your-api-key-here&search_text=user-entered-string
    private MainActivity mainActivity;
    private int count;
    private String dataURL;
    private static final String TAG = "AsyncCompanyNameLoader";

    public AsyncCompanyNameLoader(MainActivity ma, String data) {
        mainActivity = ma;
        dataURL="http://stocksearchapi.com/api/?api_key=4875423781325444ba130d3ea18a0010a97ef91b&search_text=".concat(data);
    }

    @Override
    protected void onPreExecute() {
		//Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        mainActivity.updateData(stockList);
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(dataURL);
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

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();

            if(count!=0){
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String stockName = jStock.getString("company_name");
                String shortName = jStock.getString("company_symbol");
                stockList.add(
                        new Stock(shortName, stockName));
                }
            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}

