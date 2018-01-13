package com.example.snehi.stockwatch;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by snehi on 2017-02-25.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

public TextView stockname;
public TextView stockprice;
public TextView percentage;
public TextView shortName;
public CardView cv;

public MyViewHolder(View view) {
        super(view);
        shortName=(TextView) view.findViewById(R.id.shortName);
        stockname = (TextView) view.findViewById(R.id.stockname);
        stockprice = (TextView) view.findViewById(R.id.stockPrice);
        percentage = (TextView) view.findViewById(R.id.percentage);
    }
}
