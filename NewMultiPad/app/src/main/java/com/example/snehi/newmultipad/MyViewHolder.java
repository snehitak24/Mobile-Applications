package com.example.snehi.newmultipad;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by snehi on 2017-02-25.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

public TextView title;
public TextView notes;
public TextView date;
public CardView cv;

public MyViewHolder(View view) {
        super(view);
          title = (TextView) view.findViewById(R.id.title);
          notes = (TextView) view.findViewById(R.id.notes);
          date = (TextView) view.findViewById(R.id.date);
        }



}
