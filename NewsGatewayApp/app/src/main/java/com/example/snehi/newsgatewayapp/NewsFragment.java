package com.example.snehi.newsgatewayapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by snehi on 2017-05-04.
 */

public class NewsFragment extends Fragment{


        public static final String EXTRA_MESSAGE = "ARTICLE";
        MainActivity mainActivity;
        public static final NewsFragment newInstance(News news,int n, int size)
        {
            NewsFragment f = new NewsFragment();
            Bundle bdl = new Bundle(1);
            bdl.putSerializable(EXTRA_MESSAGE, news);
            bdl.putSerializable("Number",n);
            bdl.putSerializable("Size",size);
            f.setArguments(bdl);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            News news = new News();
            news = (News) getArguments().getSerializable(EXTRA_MESSAGE);

            int n= getArguments().getInt("Number");
            int size= getArguments().getInt("Size");

            View v = inflater.inflate(R.layout.newsfragment_layout, container, false);

            TextView pageNum = (TextView) v.findViewById(R.id.pagenum);
            pageNum.setText(n+" of "+ size);
            TextView title = (TextView) v.findViewById(R.id.title);

            final String url=news.getExtraURL();
            if(title!=null)
            title.setText(news.getTitle());
            title.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });


            String auth=news.getAuthor();
            TextView author = (TextView) v.findViewById(R.id.author);
            if(news.getAuthor()!=null && !news.getAuthor().contentEquals("null")) {
                if (auth.contains("\n"))
                    auth = auth.replaceAll("\n", "");
                author.setText(auth);
            }

            if(news.getDescription()!=null && !news.getDescription().contentEquals("null")) {
                TextView desc = (TextView) v.findViewById(R.id.description);
                desc.setText(news.getDescription());
                desc.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            TextView date = (TextView) v.findViewById(R.id.date);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss");
            Date date1 = null;
            String da=news.getDate();
            if(da!=null && !da.contentEquals("null")) {
                da = da.replace("T", " ");
                da = da.replace("+"," ");
                da = da.replace("Z", "");

                try {
                    date1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(da);
                    da= new SimpleDateFormat("MMM dd,yyyy hh:mm").format(date1);

                        date.setText(da);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            final String imageURL = news.getUrl();
            final ImageView img = (ImageView) v.findViewById(R.id.image);
            Picasso.with(getActivity().getApplicationContext()).load(imageURL).fit().into(img);
           // final String url=news.getExtraURL();

            img.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            return v;
        }
}
