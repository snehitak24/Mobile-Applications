package com.example.snehi.newmultipad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    SharedPreferences pr=null;
    private ArrayList<Notes> notesList=new ArrayList<>();// Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private NotesAdapter mAdapter; // Data to recyclerview adapter
    private static final String TAG = "MainActivity";
    private static final int newNote_Req = 1;
    private static final int edit_REQ = 10;
    boolean running;

    String inpause;
    String inresume;

    int id =-1;

    private ArrayList<Notes> list=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new MyAsyncTask().execute();
    }


    class MyAsyncTask extends AsyncTask<String,String,String> { //  <Parameter, Progress, Result>
        boolean fileAvailable=false;
        @Override
        protected String doInBackground(String...params) {
            running = true;
            try {
                if (getApplicationContext().openFileInput(getString(R.string.file_name)).available() !=0) {
                    InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
                    fileAvailable=true;
                    list = readJsonStream(is);
                    if (notesList.size() == 0) {
                        for (int i = 0; i < list.size(); i++) {
                            notesList.add(list.get(i));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }}
                catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }

            running = false;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
          //  super.onPostExecute();
            Context context = getApplicationContext();
            if(fileAvailable==true)
            Toast.makeText(context,"JSON FILE LOADED",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"NO JSON FILE TO LOAD",Toast.LENGTH_SHORT).show();
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
            case R.id.addNote:

                Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent1, newNote_Req);
                mAdapter.notifyDataSetChanged();
                return true;

            case R.id.info:

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);

                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Notes m = notesList.get(pos);

        Intent i = new Intent(MainActivity.this, EditActivity.class);
        i.putExtra("position",pos);
        Log.d(TAG, "onClick: "+notesList.size());

        i.putParcelableArrayListExtra("list",notesList);
        startActivityForResult(i,edit_REQ);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        deletingnote(pos);
        return false;
    }

    protected void deletingnote(final int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Do you really want to delete this note? ");
        builder.setTitle("Delete Note");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

	public ArrayList<Notes> readJsonStream(InputStream in) throws IOException {
     JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
     try {
         return readNotesArray(reader);
     } finally {
         reader.close();
     }
 }

    public ArrayList<Notes> readNotesArray(JsonReader reader) throws IOException {
        ArrayList<Notes> list = new ArrayList<Notes>();

        reader.beginArray();
        while (reader.hasNext()) {
            list.add(readNote(reader));
        }
        reader.endArray();
        return list;
    }

    public Notes readNote(JsonReader reader) throws IOException {
        long id = -1;
        String text = null;

        Notes n = new Notes();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                n.setId(reader.nextInt());
            } else if (name.equals("title")) {
                n.setTitle(reader.nextString());
            } else if (name.equals("notes") ) {
                n.setNotes(reader.nextString());
            } else if (name.equals("date")) {
                n.setDate(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return n;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
        saveNotes();
    }

    private void saveNotes() {

        Log.d(TAG, "saving notes: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            JSONArray jarray = new JSONArray(notesList);

            writer.setIndent("  ");
            writer.beginArray();
            Log.d(TAG, "saveNotes: array size "+jarray.length());
            for(int i=0;i<jarray.length();i++) {
                writer.beginObject();

                    writer.name("id").value(notesList.get(i).getId());
                    writer.name("title").value(notesList.get(i).getTitle());
                    writer.name("notes").value(notesList.get(i).getNotes());
                    writer.name("date").value(notesList.get(i).getDate());

                writer.endObject();
            }
            writer.endArray();
            writer.close();

            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///
            for(int i=0;i<jarray.length();i++) {
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
                writer.name("id").value(notesList.get(i).getId());
                writer.name("title").value(notesList.get(i).getTitle());
                writer.name("notes").value(notesList.get(i).getNotes());
                writer.name("date").value(notesList.get(i).getDate());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveProduct: JSON:\n" + sw.toString());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newNote_Req) {
            if (resultCode == RESULT_OK) {

                String text = data.getStringExtra("TITLE");
                String text1 = data.getStringExtra("NOTE");

                DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
                Date today = new Date();
                String saveDate = df.format(today);

                if(!TextUtils.isEmpty(text)) {

                    id++;
                    notesList.add(0,new Notes(id,text,text1,saveDate )); //sort according to date
                    mAdapter.notifyDataSetChanged();
                }
                else{
                      Toast.makeText(this, "NOTE NOT SAVED\n TITLE NEEDED " , Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else if(requestCode==edit_REQ) {

           // Log.d(TAG, "onActivityResult: boolean "+b);
            if(data!=null) {
                boolean b = data.getBooleanExtra("notesChanged", false);
                if (b == true) {
                    int position = data.getIntExtra("position_new", -1);
                    String newnote = data.getStringExtra("NEWNOTE");
                    String date = data.getStringExtra("date");
                    String title =data.getStringExtra("NEWTITLE");
                    //   Log.d(TAG, "onActivityResult: outside if "+position+" "+newnote+ " "+date);
                    if (position != -1) {
                        Log.d(TAG, "onActivityResult: length " + newnote.length());

                        notesList.get(position).setDate(date);
                        notesList.get(position).setNotes(newnote);
                        if(!TextUtils.isEmpty(title))
                            notesList.get(position).setTitle(title);

                        notesList.add(0, notesList.get(position));
                        notesList.remove(position + 1);
                        mAdapter.notifyDataSetChanged();
                     
                        Log.d(TAG, "onActivityResult: notes org value " + notesList.get(position).getNotes());
                    }
                }
            }
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }
}