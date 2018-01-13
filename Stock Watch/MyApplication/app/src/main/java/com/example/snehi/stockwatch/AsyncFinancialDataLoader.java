package com.example.snehi.stockwatch;

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

public class AsyncFinancialDataLoader extends AsyncTask<String,Integer,String> {
   
    private MainActivity mainActivity;
    private int count;
    private Stock stock= new Stock();
    private String dataURL=null;
    private static final String TAG = "Async";

    public AsyncFinancialDataLoader(MainActivity ma, Stock list) {
        mainActivity = ma;
        stock=list;
        if(list!=null)
        dataURL = "http://finance.google.com/finance/info?client=ig&q=".concat(list.getShortName());
    }

    @Override
    protected void onPreExecute() {
        //  Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        Stock stock = parseJSON(s);
        mainActivity.addNewStock(stock);
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
            String sub;
                    String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains("//"))
                {   sub=line.substring(2);
                    sb.append(sub).append('\n');
                }
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

    private Stock parseJSON(String s) {
        Stock s1=null;
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String ticker = jStock.getString("t");
                String lastTradePrice = jStock.getString("l");
                String priceChangeAmt = jStock.getString("c");
                String priceChangePercent = jStock.getString("cp");
                double lastTradePric=Double.parseDouble(lastTradePrice);
                double priceChangeAm=Double.parseDouble(priceChangeAmt);
                double priceChangePercen=Double.parseDouble(priceChangePercent);
                
                s1=  new Stock(ticker,stock.getStockName(),lastTradePric,priceChangeAm,priceChangePercen);

            }
            return s1;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

