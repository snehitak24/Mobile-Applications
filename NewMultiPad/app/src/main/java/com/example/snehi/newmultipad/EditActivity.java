package com.example.snehi.newmultipad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

/**
 * Created by snehi on 2017-02-25.
 */

public class EditActivity extends AppCompatActivity {
    EditText textView1;
    EditText textView2;
    Notes note;
    String oldNotes;
    String inpause;
    String oldtitle;
    private static final int edit_REQ = 10;
    MenuItem menuitem;
    Notes n=null;
    List<Notes> noteList = new ArrayList<>();
    int pos;
    private static final String TAG = "EditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editactivity);

        textView1 = (EditText) findViewById(R.id.title);
        textView2 = (EditText) findViewById(R.id.notes);

        textView2.setMovementMethod(new ScrollingMovementMethod());
        menuitem = (MenuItem) findViewById(R.id.save);

        textView2.setCursorVisible(true);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        Intent intent =this.getIntent();
        if (intent != null) {
            //Log.d(TAG, "onCreate: In edit " + String.valueOf(intent.getStringExtra("position")));
            if(intent.getParcelableArrayListExtra("list")!=null){
                pos = intent.getIntExtra("position",pos);
                noteList=intent.getParcelableArrayListExtra("list");
                oldNotes=noteList.get(pos).getNotes();
                oldtitle=noteList.get(pos).getTitle();
                textView1.setText(noteList.get(pos).getTitle());
                textView2.setText(noteList.get(pos).getNotes());
              //  Toast.makeText(this, "SHORT " , Toast.LENGTH_SHORT).show();
            }
           // Toast.makeText(this, "SHORT OUTSIDE IF " , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuedit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
                Date today = new Date();
                String saveDate = df.format(today);
                String notes =textView2.getText().toString();
                String title=textView2.getText().toString();
                if((!notes.equals(oldNotes) && oldNotes!=null) || (!title.equals(oldtitle)&& oldtitle!=null)) {
                    Log.d(TAG, "onOptionsItemSelected: notes "+notes );
                    Intent data = new Intent();
                    data.putExtra("notesChanged", true);
                    data.putExtra("NEWTITLE",textView1.getText().toString());
                    data.putExtra("NEWNOTE", textView2.getText().toString());
                    data.putExtra("position_new",pos);
                    data.putExtra("date",saveDate);
                    setResult(RESULT_OK, data);
                    finish();
                }
                else
                {
                    Intent data1 = new Intent();
                    data1.putExtra("TITLE", textView1.getText().toString());
                    data1.putExtra("NOTE", textView2.getText().toString());

                    setResult(RESULT_OK, data1);
                    finish();
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void doneClicked() {
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
        Date today = new Date();
        String saveDate = df.format(today);
        String notes =textView2.getText().toString();
        String title=textView2.getText().toString();
        if((!notes.equals(oldNotes) && oldNotes!=null) || (!title.equals(oldtitle)&& oldtitle!=null)) {
            Log.d(TAG, "onOptionsItemSelected: notes "+notes );
            Intent data = new Intent();
            data.putExtra("notesChanged", true);
            data.putExtra("NEWTITLE",textView1.getText().toString());
            data.putExtra("NEWNOTE", textView2.getText().toString());
            data.putExtra("position_new",pos);
            data.putExtra("date",saveDate);
            setResult(RESULT_OK, data);
            finish();
        }
        else
        {
            Intent data1 = new Intent();
            data1.putExtra("TITLE", textView1.getText().toString());
            data1.putExtra("NOTE", textView2.getText().toString());

            setResult(RESULT_OK, data1);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        exitingnote();
       // super.onBackPressed();
    }


    protected void exitingnote()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doneClicked();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });

        builder.setMessage("Do you want to save the note before leaving?");
        builder.setTitle("Save the note before going back! ");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == edit_REQ) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("title");
                String text1=data.getStringExtra("notes");

                textView1.setText(text);
                textView2.setText(text1);
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }


}