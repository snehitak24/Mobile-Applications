package com.example.snehi.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockWatchTable";
    ///DB Columns
    private static final String SYMBOL = "StockSymbol";

    private static final String COMPANY = "CompanyName";

    private static final String LASTPRICE = "LastStockPrice";

    private static final String PRICECHANGE = "PriceChange";

    private static final String PRICEPERCENTAGE = "PricePercentage";

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null ," +
                    COMPANY + " TEXT not null unique, " +
                    LASTPRICE + " TEXT not null, " +
                    PRICECHANGE + " TEXT not null, " +
                    PRICEPERCENTAGE + " TEXT not null);";

    private SQLiteDatabase database;
   // private static final String DATABASE_ALTER_TABLE_FOR_V2 = "ALTER TABLE "
   //         + TABLE_NAME + " ADD COLUMN " + AREA + " int not null default 0;";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2) {
         //   db.execSQL(DATABASE_ALTER_TABLE_FOR_V2);
        }
        if (newVersion == 3) {
         //   db.execSQL(DATABASE_ALTER_TABLE_FOR_V3);
        }
    }

    public ArrayList<Stock> loadStocks() {

        Log.d(TAG, "loadStocks: LOADING STOCK DATA FROM DB");
        ArrayList<Stock> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{SYMBOL, COMPANY, LASTPRICE, PRICECHANGE, PRICEPERCENTAGE}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                String lp= cursor.getString(2);
                String cprice =cursor.getString(3);
                String cp= cursor.getString(4);

                double cpr=Double.parseDouble(cprice);
                double c=Double.parseDouble(cp);
                double l=Double.parseDouble(lp);

                stocks.add(new Stock(symbol,company,l,cpr,c));
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadStock: DONE LOADING STOCK DATA FROM DB");

        return stocks;
    }

    public void addStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getShortName());
        values.put(COMPANY, stock.getStockName());
        values.put(LASTPRICE, stock.getStockPrice());
        values.put(PRICECHANGE, stock.getStockPriceChange());
        values.put(PRICEPERCENTAGE, stock.getPercentage());

      //  deleteCountry(country.getName());
        long key = database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addSTOCK: " + key);
    }


    public void deleteStock(String symbol) {
        Log.d(TAG, "deleteStock: " + symbol);
        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{symbol});
        Log.d(TAG, "deleteStock: " + cnt);
    }

    public void updateCountry(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getShortName());
        values.put(COMPANY, stock.getStockName());
        values.put(LASTPRICE, stock.getStockPrice());
        values.put(PRICECHANGE, stock.getStockPriceChange());
        values.put(PRICEPERCENTAGE, stock.getPercentage());

        long key = database.update(
                TABLE_NAME, values, COMPANY + " = ?", new String[]{stock.getStockName()});

        Log.d(TAG, "updateCountry: " + key);
    }

    public void dumpLog() {
       Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public void updateStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getShortName());
        values.put(COMPANY, stock.getStockName());
        values.put(LASTPRICE, stock.getStockPrice());
        values.put(PRICECHANGE, stock.getStockPriceChange());
        values.put(PRICEPERCENTAGE, stock.getPercentage());

        long key = database.update(
                TABLE_NAME, values, SYMBOL + " = ?", new String[]{stock.getShortName()});

        Log.d(TAG, "updateCountry: " + key);
    }
}
