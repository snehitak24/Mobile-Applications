package com.example.snehi.knowyourgov;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by snehi on 2017-04-12.
 */

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private ImageView img;
    private TextView name;
    private TextView post;
    private ConstraintLayout act;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoactivity);
        img = (ImageView) findViewById(R.id.img);
        Intent i = getIntent();
        String url = i.getStringExtra("URL");
        String post1 = i.getStringExtra("POST");
        String name1 = i.getStringExtra("NAME");
        String party = i.getStringExtra("PARTY");
        loadImage(url);

        act = (ConstraintLayout) findViewById(R.id.photoactivity);
        name = (TextView) findViewById(R.id.name);
        name.setText(name1);
        post = (TextView) findViewById(R.id.post);
        post.setText(post1);
        if (party != null) {
            if (party.equals("Republican"))
                act.setBackgroundColor(Color.RED);
            else if (party.equals("Democratic"))
                act.setBackgroundColor(Color.BLUE);
            else
                act.setBackgroundColor(Color.BLACK);
        } else
            act.setBackgroundColor(Color.BLACK);

    }

    private void loadImage(final String imageURL) {
        Log.d(TAG, "loadImage: " + imageURL);
        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
				// Here we try https if the http image attempt failed
                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .fit()
                            .placeholder(R.drawable.placeholder)
                            .into(img);
                }
            }).build();
            picasso.load(imageURL)
                    .error(R.drawable.brokenimage)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(img);
        } else {
            Picasso.with(this).load(imageURL)
                    .error(R.drawable.brokenimage)
                    .fit()
                    .placeholder(R.drawable.missingimage)
                    .into(img);
        }
    }
}