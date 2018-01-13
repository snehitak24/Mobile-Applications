package com.example.snehi.newmultipad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by snehi on 2017-02-25.
 */

public class InfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoactivity);

        TextView textView1 = (TextView) findViewById(R.id.textshow);
        TextView textView2 = (TextView) findViewById(R.id.textshow2);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            textView1.setText(" MULTI NOTEPAD " );
            textView2.setText("\u00A9 2017 Snehita Kolapkar");
            textView2.append("\n \n Version 1.0");
        }


    }
}