package com.example.snehi.knowyourgov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by snehi on 2017-04-06.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView textView1 = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();
		if (intent.hasExtra(Intent.EXTRA_TEXT)) {
			String text = intent.getStringExtra(Intent.EXTRA_TEXT);
			textView1.setText(" KNOW YOUR GOVERNMENT" );
			textView2.setText("\u00A9 2017, Snehita Kolapkar");
			textView2.append("\n \n Version 1.0");
		}

}}
