package com.example.snehi.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<Stock> stocksList=new ArrayList<>();// Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private StockAdapter mAdapter; // Data to recyclerview adapter
    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    private DatabaseHandler databaseHandler;
    ArrayList<Stock> finalList= new ArrayList<>();
    private static String stockShortName;
    AsyncCompanyNameLoader as;
    AsyncFinancialDataLoader af;
    String userText=null;
    private ArrayList<Stock> tempstocksList=new ArrayList<>();
    private ArrayList<Stock> tempStartup = new ArrayList<>();
    private AsyncFinancialDataLoader afStart;
    boolean refresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new StockAdapter(stocksList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHandler = new DatabaseHandler(this);

        databaseHandler.dumpLog();
        ArrayList<Stock> list = databaseHandler.loadStocks();
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if(list!=null)

                for(int i =0;i<list.size();i++)
                {
                   // stocksList.remove(i);
                    afStart =new AsyncFinancialDataLoader(this,list.get(i));
                    afStart.execute();
                }
        }
        else{
            Toast.makeText(this,"No active internet connection",Toast.LENGTH_SHORT).show();

            stocksList.addAll(list);
            Collections.sort(stocksList);
            mAdapter.notifyDataSetChanged();
        }
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
    }

    private void doRefresh() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            finalList.addAll(stocksList);
            Log.d(TAG, "onCreate: size: "+finalList.size());
            stocksList.clear();
            mAdapter.notifyDataSetChanged();
            if(finalList!=null) {
                for (int i = 0; i < finalList.size(); i++) {

                    afStart = new AsyncFinancialDataLoader(this, finalList.get(i));
                    refresh=true;
                    afStart.execute();
                }
                swiper.setRefreshing(false);
            }
            if(finalList!=null)
                finalList.clear();
            //Toast.makeText(this, "List content refreshed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            swiper.setRefreshing(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks cannot be updated without a network connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addstock:
                Log.d(TAG, "onOptionsItemSelected: on add");
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    builder.setView(et);
                    userText=et.getText().toString().trim();
                    // builder.setIcon(R.drawable.icon1);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            databaseHandler.dumpLog();
                            as = new AsyncCompanyNameLoader(MainActivity.this, et.getText().toString().trim());
                            as.execute();
                            Date date = new Date();
                            Log.d(TAG, "onClick: " + tempstocksList.size() + " " + date + " " + as.getStatus());
                        }

                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.setTitle("Stock Selection");
                    builder.setMessage("Please enter a stock symbol");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("No Network Connection");
                    builder.setMessage("Stocks cannot be added without a network connection");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;

                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
       // deletingstock(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(stocksList.get(pos).getShortName());
                stocksList.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Delete Stock " + stocksList.get(pos).getStockName() + "?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        if(stocksList.size()>0 && stocksList.get(pos)!=null) {
            String stockName = stocksList.get(pos).getShortName();
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                String url = "http://www.marketwatch.com/investing/stock/".concat(stockName);
                clickStock(url);

            } else {
                Toast.makeText(this, "You are NOT Connected to the Internet!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else{
        }

    }
    public void clickStock(String urlStock) {
        String url = urlStock;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void updateData(ArrayList<Stock> cList) {
        if(cList!=null){
        for(int i=0;i<cList.size();i++)
        Log.d(TAG, "updateData: "+cList.get(i));
        tempstocksList.addAll(cList);

        Date date=new Date();
        Log.d(TAG, "updateData: "+date);

        if(tempstocksList.size()>1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Make a selection");
            // builder.setIcon(R.drawable.icon2);
            final Stock[] stock = tempstocksList.toArray(new Stock[tempstocksList.size()]);
            final CharSequence[] stockArray = new CharSequence[tempstocksList.size()];
            for (int i = 0; i < tempstocksList.size(); i++)
                stockArray[i] = tempstocksList.get(i).getShortName() + " - " + tempstocksList.get(i).getStockName();
            //   ListAdapter list = new ArrayAdapter<Stock>();

            builder.setItems(stockArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    af = new AsyncFinancialDataLoader(MainActivity.this, tempstocksList.get(which));
                    af.execute();
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        }
            else{
            if(tempstocksList.size()==1) {
                af = new AsyncFinancialDataLoader(MainActivity.this, tempstocksList.get(0));
                af.execute();
            }
        }
        }
        else if(cList==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Symbol Not found: "+userText);
            builder.setMessage("Data for stack symbol");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void addNewStock(Stock stock){

       // b=stocksList.contains(stock);
        for(int i =0;i<stocksList.size();i++)
            Log.d(TAG, "stock: "+stocksList.get(i).toString());

        if(stock!=null){
            for(int i =0;i<stocksList.size();i++)
                if (stocksList.get(i).getStockName().equals(stock.getStockName())){
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Duplicate Stock");
                        builder.setMessage("Stock Symbol "+stock.getStockName()+" is already present");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        tempstocksList.clear();
                        return;
                    }
                }
        }

        if(stock!=null){

                stocksList.add(stock);
                Collections.sort(stocksList);
                Log.d(TAG, "addNewStock: "+refresh);
                if(refresh==false) {
                    databaseHandler.addStock(stock);
                    Log.d(TAG, "addNewStock: "+stock.getStockName()+" ");
                }
                else{
					databaseHandler.updateStock(stock);
					mAdapter.notifyDataSetChanged();
					tempstocksList.clear();
				}
        }
    }
}
