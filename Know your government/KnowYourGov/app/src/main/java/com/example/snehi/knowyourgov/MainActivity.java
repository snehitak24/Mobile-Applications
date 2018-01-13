package com.example.snehi.knowyourgov;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private List<LinkedHashMap<String,Official>> officialList=new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private OfficialAdapter mAdapter; // Data to recyclerview adapter
    private static final String TAG = "MainActivity";
    private TextView zip;
    private Locator locator;
    String location;
    String address;
    AsyncCivicInfoLoader af;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zip = (TextView) findViewById(R.id.location);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new OfficialAdapter(officialList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            locator = new Locator(this);
            boolean loc = locator.determineLocationNetwork();
            Log.d(TAG, "onCreate: " + loc);
            if (loc == true) {
                locator.determineLocationNetwork();
                af = new AsyncCivicInfoLoader(MainActivity.this, address);
                af.execute();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Official List cannot be loaded without a network connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        LinkedHashMap<String,Official> hmap= officialList.get(pos);
        String name = (String)(hmap.keySet().toArray())[0];
        Official m = hmap.get( (hmap.keySet().toArray())[ 0 ] );
      
        Intent i = new Intent(MainActivity.this, OfficialActivity.class);
        i.putExtra("Official",m);
        i.putExtra("OfficialName",name);
        location=zip.getText().toString();
        i.putExtra("Location",location);
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v){

        onClick(v);

        return false;
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.location:
                Log.d(TAG, "onOptionsItemSelected: on add");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderEmpty = new AlertDialog.Builder(this);

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(et);
                String locGeo;

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    if(!TextUtils.isEmpty(et.getText())){
                        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                        try {
                            List<Address> addresses = null;

                            String loc = et.getText().toString();
                            addresses = geocoder.getFromLocationName(loc, 1);
                            officialList.clear();
                            if(addresses!=null)
                            af = new AsyncCivicInfoLoader(MainActivity.this, et.getText().toString());
                            af.execute();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        {
                            builderEmpty.setTitle("Location cannot be empty");

                            builderEmpty.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    
                                }
                            });
                            AlertDialog dialogNull = builderEmpty.create();
                            dialogNull.show();
                            return;
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


                builder.setTitle("Enter a city, state or zip value");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setData(double lat, double lon) {
        address = doAddress(lat, lon);
        zip = (TextView) findViewById(R.id.location);
        
        Log.d(TAG, "setData: "+address);
    }

    public void updateData(List<LinkedHashMap<String,Official>> offList) {
        if(offList!=null) {
            officialList.addAll(offList);
            mAdapter.notifyDataSetChanged();
        }
    }


    public String doAddress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			StringBuilder sb = new StringBuilder();

			for (Address ad : addresses) {
				Log.d(TAG, "doLocation: " + ad);
				if(ad.getPostalCode()!=null)
					sb.append(ad.getLocality()+", "+ad.getAdminArea()+","+ad.getPostalCode());
				else if(ad.getLocality()!=null)
					sb.append(ad.getLocality()+", "+ad.getAdminArea());
				else{
					sb.append(ad.getAdminArea());
				}
			}
			Log.d(TAG, "doAddress: sb "+sb.toString());
			return sb.toString();
		 } catch (IOException e) {
			 Toast.makeText(this, "Cannot acquire ZIP code from Lat/Lon.\n\nNetwork resources unavailable", Toast.LENGTH_LONG).show();
		 }
		 return null;

 }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if(locator!=null)
            locator.shutDown();
    }

    public void setAddress(String city,String state,String zipcode){
        zip = (TextView) findViewById(R.id.location);

        if(zipcode!=null && !zipcode.isEmpty()){
            Log.d(TAG, "setAddress: "+city.length());
            zip.setText(city+", "+state+", "+zipcode);
        }
        else if(city!=null && !city.isEmpty()) {
            Log.d(TAG, "setAddress: "+city.length());
            zip.setText(city + ", " + state);
        }
        else if(state!=null && !state.isEmpty()){
            Log.d(TAG, "setAddress: "+state);
            zip.setText(state);
        }
        else
        {
            final AlertDialog.Builder builderEmpty = new AlertDialog.Builder(this);
            builderEmpty.setTitle("No location at this address");
            builderEmpty.setMessage("Enter new city, state or zipcode to continue.");
            AlertDialog dialog = builderEmpty.create();
            dialog.show();
            Log.d(TAG, "setAddress: "+address);
           // return true;
        }
    }
	
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocationNetwork();
                        Log.d(TAG, "onRequestPermissionsResult: add: "+address);
                        af = new AsyncCivicInfoLoader(MainActivity.this, address);
                        af.execute();

                    } else {
                        zip.setText("Location inaccessible");
                        Toast.makeText(MainActivity.this,"Need location access permission to run the app",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Calling super onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }
}