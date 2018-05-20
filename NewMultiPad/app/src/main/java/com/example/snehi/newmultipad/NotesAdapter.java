package com.example.snehi.newmultipad;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by snehi on 2017-02-26.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private static final String TAG = "NotesAdapter";
        private List<Notes> notesList;
        private MainActivity mainAct;
        View view;
        Notes current;
		
		public NotesAdapter(List<Notes> notesList, MainActivity ma) {
            this.notesList = notesList;
            mainAct = ma;
        }

        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: MAKING NEW");
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
            itemView.setOnClickListener(mainAct);
            itemView.setOnLongClickListener(mainAct);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Notes notes = notesList.get(position);
            holder.title.setText(notes.getTitle());
            holder.date.setText(notes.getDate());
            if(notes.getNotes().length()>80)
                holder.notes.setText(notes.getNotes().substring(0,50)+"...");
            else
                holder.notes.setText(notes.getNotes());
            Log.d(TAG, "onBindViewHolder: title for position "+position+"is "+notes.getTitle());
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }
}


