package com.example.snehi.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by snehi on 2017-03-07.
 */

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private static final String TAG = "StockAdapter";
        private List<Stock> stocksList;
        private MainActivity mainAct;
        View view;
        Stock current;
        public StockAdapter(List<Stock> stockList, MainActivity ma) {
            this.stocksList = stockList;
            mainAct = ma;
        }

        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: MAKING NEW");
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row, parent, false);
            itemView.setOnClickListener(mainAct);
            itemView.setOnLongClickListener(mainAct);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Stock stock = stocksList.get(position);

            if(stock.getStockPriceChange()<0) {
                holder.percentage.setTextColor(Color.RED);
                holder.percentage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
                holder.percentage.setText(String.valueOf(stock.getStockPriceChange()) + " " + "(" + String.valueOf(stock.getPercentage()) + "%)");

                holder.shortName.setTextColor(Color.RED);
                holder.shortName.setText(stock.getStockName());

                holder.stockname.setTextColor(Color.RED);
                holder.stockname.setText(stock.getShortName());
                
                holder.stockprice.setTextColor(Color.RED);
                holder.stockprice.setText(String.valueOf(stock.getStockPrice()));
            }
            else
            {
                holder.shortName.setTextColor(Color.GREEN);
                holder.shortName.setText(stock.getStockName());

                holder.stockname.setTextColor(Color.GREEN);
                holder.stockname.setText(stock.getShortName());

                holder.stockprice.setTextColor(Color.GREEN);
                holder.stockprice.setText(String.valueOf(stock.getStockPrice()));

                holder.percentage.setTextColor(Color.GREEN);
                holder.percentage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                holder.percentage.setText(String.valueOf(stock.getStockPriceChange()) + " " + "(" + String.valueOf(stock.getPercentage()) + "%)");
            }
        }

        @Override
        public int getItemCount() {
            return stocksList.size();
        }
    }




