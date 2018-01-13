package com.example.snehi.newsgatewayapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView img;

    public HashMap<String,Source> hashMap = new HashMap<>();
    public HashSet<String> categories = new HashSet<>();

    ArrayList<News> articleList = new ArrayList<>();
    ArrayList<Source> sourceList = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] categoriesArray;

    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String ACTION_MSG_TO_SVC = "ACTION_MSG_TO_SVC";

    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private android.support.v4.view.ViewPager pager;
    NewsSourceDownloader nsd;
    NewsReceiver receiver;
    Source currentSource=new Source();
    private ViewPager mPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        receiver = new NewsReceiver();

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(receiver, filter1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        mPager = (ViewPager) view.findViewById(R.id.viewpager);
                        //mPager.setBackgroundColor(Color.WHITE);

                        if(!sourceList.isEmpty()) {
                            currentSource = sourceList.get(position);
                            Intent intent = new Intent();
                            intent.setAction(ACTION_MSG_TO_SVC);
                            intent.putExtra("SOURCEOBJ", currentSource.getId().toString());
                            Log.d(TAG, "onItemClick: "+currentSource.getId().toString());
                            sendBroadcast(intent);
                            selectItem(position);

                        }
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        String s=null;
        nsd = new NewsSourceDownloader(MainActivity.this,s);
        nsd.execute();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
        nsd = new NewsSourceDownloader(MainActivity.this,item.getTitle().toString());
        nsd.execute();
        mDrawerLayout.closeDrawer(mDrawerList);
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        Intent intent = new Intent(MainActivity.this, NewsReceiver.class);
        stopService(intent);
        super.onDestroy();
    }


    private void selectItem(int position) {
       // Toast.makeText(this, sourceList.get(position), Toast.LENGTH_SHORT).show();
        //Set<Source> entry = hashMap.;
        setTitle(sourceList.get(position).getName());
       // reDoFragments(articleList);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    if (intent.hasExtra("storylist")) {
                    articleList = intent.getParcelableArrayListExtra("storylist");
                    //Toast.makeText(MainActivity.this, "Broadcast Message A Received: ", Toast.LENGTH_SHORT).show();
                    //break;

                        Log.d(TAG, "onReceive: "+articleList.size());
                    reDoFragments(articleList);
                    //Toast.makeText(MainActivity.this, "Broadcast Message A Received: ", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
        }
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }
    }

    private void reDoFragments(ArrayList<News> articleList) {
        //getActionBar().setTitle(currentSource);
        Log.d(TAG, "reDoFragments: "+articleList.size());
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);
			fragments.clear();

        for (int i = 0; i < articleList.size(); i++) {
            fragments.add(NewsFragment.newInstance(articleList.get(i),i+1,articleList.size()));
            pageAdapter.notifyChangeInPosition(i);
        }
        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }

    public void setSources(ArrayList<Source> list, HashSet<String> set) {
        sourceList.clear();
        hashMap.clear();
        if (list != null) {
            sourceList.addAll(list);
            // Log.d(TAG, "setSources: "+sourceList.get(0).getName());
            for (int i = 0; i < list.size(); i++) {
                hashMap.put(list.get(i).getName(), list.get(i));
            }

            int i = 0;
            categoriesArray = new String[set.size()];
            for (String category : set) {
                categoriesArray[i] = category.toString();
                i++;
            }

            mDrawerList.setAdapter(new ArrayAdapter<>(this,
                    R.layout.drawer_list_item, list));
        }
    }
}
