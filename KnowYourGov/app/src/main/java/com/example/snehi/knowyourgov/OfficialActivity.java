package com.example.snehi.knowyourgov;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import static com.example.snehi.knowyourgov.R.id.imageView;

/**
 * Created by snehi on 2017-04-08.
 */

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private ImageView img;
    private TextView addr;
    private TextView phone1;
    private TextView email1;
    private TextView website;
    private TextView location;
    private TextView name1;
    private TextView post;
    Official o=null;
    private ImageView you;
    private ImageView face;
    private ImageView goog;
    private ImageView tweet;
    private ConstraintLayout act;
    String name;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.officialactivity);
        Intent intent = getIntent();

        if (intent.hasExtra("Official")) {
            o = (Official) intent.getSerializableExtra("Official");
            loadImage(o.getPhotoUrl());                  //Enter pic url
        }

        addr = (TextView) findViewById(R.id.AddressValue);
        post = (TextView) findViewById(R.id.post);
        phone1 = (TextView) findViewById(R.id.PhoneValue);
        email1 = (TextView) findViewById(R.id.EmailValue);
        website = (TextView) findViewById(R.id.WebsiteValue);
        location = (TextView) findViewById(R.id.location);
        name1 = (TextView) findViewById(R.id.name);
        face = (ImageView) findViewById(R.id.facebook);
        goog = (ImageView) findViewById(R.id.google);
        tweet = (ImageView) findViewById(R.id.twitter);
        you = (ImageView) findViewById(R.id.youtube);
        act = (ConstraintLayout) findViewById(R.id.officialactivity);

        face.setVisibility(View.INVISIBLE);
        goog.setVisibility(View.INVISIBLE);
        tweet.setVisibility(View.INVISIBLE);
        you.setVisibility(View.INVISIBLE);

        if (intent.hasExtra("Location")) {
            String loc = intent.getStringExtra("Location");
            location.setText(loc);
        }

        if (intent.hasExtra("OfficialName")) {
            name = intent.getStringExtra("OfficialName");
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                name1.setText(o.getOfficialName()+"\t");
                name1.setTextSize(14);
                if(o.getParty()!=null)
                    name1.append("("+o.getParty()+")");
            }
            else {
                name1.setText(o.getOfficialName() + "\n");
                name1.setTextSize(14);
                if (o.getParty() != null)
                    name1.append("(" + o.getParty() + ")");
            }
            post.setText(name);
            if (o!=null && o.getParty()!=null){
            if(o.getParty().equals("Republican"))
                act.setBackgroundColor(Color.RED);
            else if(o.getParty().equals("Democratic"))
                act.setBackgroundColor(Color.BLUE);
            else
                act.setBackgroundColor(Color.BLACK);
            }
            else
                act.setBackgroundColor(Color.BLACK);
        }
        if (intent.hasExtra("Official")) {
            String address = o.getAddress();
            if (address != null){
                addr.setText(address);
                Log.d(TAG, "onCreate: "+address);
                Linkify.addLinks(((TextView) findViewById(R.id.AddressValue)), Linkify.MAP_ADDRESSES);
                addr.setLinkTextColor(Color.WHITE);

            }
            else {
                addr.setText("No data found");
            }

        }
        if (intent.hasExtra("Official")) {
            String phone[] = o.getPhone();
            //Log.d(TAG, "onCreate: phone "+phone);
            phone1.setText("");
            if (phone != null) {
                for (int i = 0; i < phone.length; i++){
                    phone1.append(phone[i] + "\t");
                    Linkify.addLinks(((TextView) findViewById(R.id.PhoneValue)), Linkify.PHONE_NUMBERS);
                    phone1.setLinkTextColor(Color.WHITE);
                }
            } else {
                phone1.setText("No data found");
            }
        }
        if (intent.hasExtra("Official")) {
            String email[] = o.getEmail();
            email1.setText("");
            if (email != null) {
                for (int i = 0; i < email.length; i++) {
                    email1.append(email[i] + "\t");
                    Linkify.addLinks(((TextView) findViewById(R.id.EmailValue)), Linkify.EMAIL_ADDRESSES);
                    email1.setLinkTextColor(Color.WHITE);
                }
            } else {
                email1.setText("No data found");
            }
        }
        if (intent.hasExtra("Official")) {
            String webs[] = o.getUrl();
            // Log.d(TAG, "onCreate: phone "+phone);
            website.setText("");
            if (webs != null) {
                for (int i = 0; i < webs.length; i++){
                    website.append(webs[i] + "\t");
                    Linkify.addLinks(((TextView) findViewById(R.id.WebsiteValue)), Linkify.WEB_URLS);
                    website.setLinkTextColor(Color.WHITE);
                }
            } else {
                website.setText("No data found");
            }
        }

        if (o != null) {
            String channels[][] = o.getChannels();
            if (channels != null) {
                for (int i = 0; i < channels.length; i++)
                    if ((channels[i][0]).equals("YouTube")) {
                        you.setVisibility(View.VISIBLE);
                    } else if ((channels[i][0]).equals("Facebook")) {
                        face.setVisibility(View.VISIBLE);
                    } else if ((channels[i][0]).equals("Twitter")) {
                        tweet.setVisibility(View.VISIBLE);
                    } else {
                        goog.setVisibility(View.VISIBLE);
                    }
            }
        }

    }

    private void loadImage(final String imageURL) {
        //     compile 'com.squareup.picasso:picasso:2.5.2'
        img =(ImageView) findViewById(R.id.photo);
        Log.d(TAG, "loadImage: " + imageURL);

        if (o.getPhotoUrl()!= null) {
           final Picasso picasso1 = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
				// Here we try https if the http image attempt failed

                    final String changedUrl = o.photoUrl.replace("http:", "https:"); ///////////////
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(img);
                }
            }).build();
            picasso1.load(o.photoUrl)
                    .error(R.drawable.brokenimage)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(img);
        } else {
           Picasso.with(this).load(o.photoUrl)
                    .error(R.drawable.brokenimage)
                    .fit()
                    .placeholder(R.drawable.missingimage)
                    .into(img);
        }
    }

    public void youTubeClicked(View v) {
        boolean isLink=false;
        String channels[][] = o.getChannels();
        if (channels != null) {
            for(int i=0;i<channels.length;i++)
                if((channels[i][0]).equals("YouTube")){
                    String name = channels[i][1];
                    Intent intent = null;
                    you=(ImageView) findViewById(R.id.youtube);
                    you.setVisibility(View.VISIBLE);
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse("https://www.youtube.com/" + name));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/" + name)));
                }
            }
        }

    }
    public void googlePlusClicked(View v) {
        boolean isLink=false;
        String channels[][] = o.getChannels();
        if (channels != null) {
            for(int i=0;i<channels.length;i++)
                if((channels[i][0]).equals("GooglePlus")){
                    String name = channels[i][1];
                    Intent intent = null;
                    isLink=true;
                    goog=(ImageView) findViewById(R.id.google);
                    goog.setVisibility(View.VISIBLE);
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
            }
            }
        }

    }

    public void twitterClicked(View v) {
        boolean isLink=false;
        String channels[][] = o.getChannels();
        if (channels != null) {
            for (int i = 0; i < channels.length; i++)
                if ((channels[i][0]).equals("Twitter")) {
                    String name = channels[i][1];
                    isLink=true;
                    Intent intent = null;
                    tweet=(ImageView) findViewById(R.id.twitter);
                    tweet.setVisibility(View.VISIBLE);
                    try {
                        // get the Twitter app if possible
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
                    }
                    startActivity(intent);
                }
        }

    }
    public void facebookClicked(View v) {
        boolean isLink=false;
        String channels[][] = o.getChannels();
        if (channels != null) {
            for (int i = 0; i < channels.length; i++)
                if ((channels[i][0]).equals("Facebook")) {
                    String name = channels[i][1];
                    isLink=true;
                    face=(ImageView) findViewById(R.id.facebook);
                    face.setVisibility(View.VISIBLE);
                    String FACEBOOK_URL = "https://www.facebook.com/" + name;// + <the official’s facebook id from download>;
                    String urlToUse;
                    PackageManager packageManager = getPackageManager();
                    try {
                        int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                        if (versionCode >= 3002850) { //newer versions of fb app
                            urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                        } else { //older versions of fb app
                            urlToUse = "fb://page/" +"";// channels.get("Facebook");
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        urlToUse = FACEBOOK_URL; //normal web url
                    }
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    facebookIntent.setData(Uri.parse(urlToUse));
                    startActivity(facebookIntent);
                }

        }

    }
    public void imageClicked(View v){
        if(o.getPhotoUrl()!=null) {
            Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
            intent.putExtra("POST", name);
            intent.putExtra("NAME", o.getOfficialName());
            intent.putExtra("URL", o.getPhotoUrl());
            intent.putExtra("PARTY", o.getParty());
            startActivity(intent);
        }
    }
}
