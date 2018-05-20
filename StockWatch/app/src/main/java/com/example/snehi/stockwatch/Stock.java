package com.example.snehi.stockwatch;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by snehi on 2017-03-07.
 */

public class Stock  implements Serializable, Comparable<Stock>  {

    String shortName;
    String stockName;
    double stockPrice;
    double percentage;
    double stockPriceChange;

    public Stock(String shortName, String stockName, double stockPrice, double stockPriceChange,double percentage) {
        this.shortName = shortName;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockPriceChange = stockPriceChange;
        this.percentage = percentage;
    }

    public Stock( String shortName,String stockName) {
        this.stockName = stockName;
        this.shortName = shortName;
    }

    public Stock( ) {

    }

    protected Stock(Parcel in) {
        readFromParcel(in);
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public double getStockPriceChange() {
        return stockPriceChange;
    }

    public void setStockPriceChange(double stockPriceChange) {
        this.stockPriceChange = stockPriceChange;
    }

    private void readFromParcel(Parcel in) {
        shortName=in.readString();
        stockName = in.readString();
        stockPrice = in.readDouble();
        percentage=in.readDouble();
    }

    @Override
    public String toString() {
        return "Stock{" +
                "shortName='" + shortName + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                ", percentage=" + percentage +

                '}';
    }

    @Override
    public int compareTo(Stock o) {
        String name =((Stock)o).getShortName();
        return this.getShortName().compareTo(name);
    }
}
