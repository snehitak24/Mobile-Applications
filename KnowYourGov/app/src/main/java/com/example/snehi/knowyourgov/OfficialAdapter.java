package com.example.snehi.knowyourgov;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by snehi on 2017-04-06.
 */

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "OfficialAdapter";
    private List<LinkedHashMap<String,Official>> officialList;
    private MainActivity mainAct;
    View view;
    Official current;

    public OfficialAdapter(List<LinkedHashMap<String,Official>> officialList, MainActivity ma) {
        this.officialList = officialList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.officialrow, parent, false);
        itemView.setOnClickListener(mainAct);
       
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LinkedHashMap<String,Official> hmap= officialList.get(position);
        String name = (String)(hmap.keySet().toArray())[0];
        Official off= hmap.get( (hmap.keySet().toArray())[ 0 ] );
        holder.officialName.setText(name);
        if(off.getParty()!=null)
            holder.official.setText(off.getOfficialName()+ " ("+off.getParty()+")");
        else
            holder.official.setText(off.getOfficialName());
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}




