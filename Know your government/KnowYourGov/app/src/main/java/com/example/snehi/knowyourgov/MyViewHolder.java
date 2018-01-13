package com.example.snehi.knowyourgov;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by snehi on 2017-02-25.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

	public TextView officialName;
	public TextView official;

	public MyViewHolder(View view) {
			super(view);

			officialName=(TextView) view.findViewById(R.id.officialName);
			official =(TextView) view.findViewById(R.id.official);
		}
	}
