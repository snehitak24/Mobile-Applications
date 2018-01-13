package com.example.snehi.knowyourgov;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AsyncCivicInfoLoader extends AsyncTask<String,Integer,String> {
    private MainActivity mainActivity;
    private int count;

    private String dataURL;
    private static final String TAG = "AsyncCompanyNameLoader";
    private ArrayList<Official> officialList=new ArrayList<>();
    private ArrayList<Offices> officesList=new ArrayList<>();
    List<LinkedHashMap<String,Official>> list=new ArrayList<>();
    String city;
    String state;
    String zip;

    public AsyncCivicInfoLoader(MainActivity ma, String zip) {
        mainActivity = ma;
        dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDVFgQau5xCpTS4ry4IwJZ5EklLkUV2l4E&address="+zip;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        List<LinkedHashMap<String,Official>> officialList = parseJSON(s);
        mainActivity.updateData(officialList);
        mainActivity.setAddress(city,state,zip);
    }


    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }


    private List<LinkedHashMap<String,Official>> parseJSON(String s) {
        Log.d(TAG, "parseJSON: "+s);
        ArrayList<Official> officialList = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(s);
            JSONObject location = jObject.getJSONObject("normalizedInput");
            city = location.getString("city");
            state = location.getString("state");
            zip = location.getString("zip");

            Log.d(TAG, "parseJSON: "+city);

            JSONArray jOfficials = jObject.getJSONArray("officials");
            count = jOfficials.length();
            Log.d(TAG, "parseJSON: In official "+count);

                if(count!=0){
                    for (int i = 0; i < jOfficials.length(); i++) {
                        JSONObject jOfficial = (JSONObject) jOfficials.get(i);
                        String officialName = jOfficial.getString("name");
                        String address=null;
                        if(jOfficial.has("address")){
                            JSONArray jaddr =  jOfficial.getJSONArray("address");


                            for (int j = 0; j < jaddr.length(); j++) {
                                JSONObject jObj = (JSONObject) jaddr.get(j);
                                String line2="";
                                if(jObj.has("line2")){
                                line2=jObj.getString("line2")+"";
                                address = jObj.getString("line1") +",\n"+
                                     line2  +",\n"+ jObj.getString("city") +", " +jObj.getString("state") +", "+ jObj.getString("zip");
                                }
                                else
                                {

                                    address = jObj.getString("line1") +",\n"+ jObj.getString("city") +", " +jObj.getString("state") +", "+ jObj.getString("zip");
                                }
                            }
                        }
                        String party=null;
                        if(jOfficial.has("party"))
                        party = jOfficial.getString("party");


                        JSONArray phones1=null;
                        String phones[]=null;
                        if(jOfficial.has("phones")) {
                            phones1 = jOfficial.getJSONArray("phones");
                            phones = new String[phones1.length()];
                            for (int k = 0; k < phones1.length(); k++) {
                                phones[k] = phones1.getString(k);
                            }
                        }
                        JSONArray url1=null;
                        String urls[]=null;
                        if(jOfficial.has("urls")) {
                            url1 = jOfficial.getJSONArray("urls");
                            urls = new String[url1.length()];
                            for (int k = 0; k < url1.length(); k++) {
                                urls[k] = url1.getString(k);
                            }
                        }

                        JSONArray email1=null;
                        String emails[]=null;
                        if(jOfficial.has("emails")) {
                            email1 = jOfficial.getJSONArray("emails");
                            emails = new String[email1.length()];
                            for (int k = 0; k < email1.length(); k++) {
                                emails[k] = email1.getString(k);
                            }
                        }
                        String photourl = null;
                        if(jOfficial.has("photoUrl"))
                            photourl=jOfficial.getString("photoUrl");
                        JSONArray channels=null;
                        String channel[][]=null;
                        if(jOfficial.has("channels")) {
                            channels = jOfficial.getJSONArray("channels");
                             channel= new String[channels.length()][2];
                            for (int j = 0; j < channels.length(); j++) {
                                JSONObject jChannels = (JSONObject) channels.get(j);
                                channel[j][0] = jChannels.getString("type");
                                channel[j][1] = jChannels.getString("id");
                            }
                        }
                        officialList.add(new Official(officialName,address,party,phones,urls,emails,photourl,channel));
                    }

                    }
            JSONArray jOffices = jObject.getJSONArray("offices");
            count = jOffices.length();
            Log.d(TAG, "parseJSON: In official "+count);

            if(count!=0) {
                for (int i = 0; i < jOffices.length(); i++) {
                    JSONObject jOffice = (JSONObject) jOffices.get(i);
                    String name = jOffice.getString("name");
                    JSONArray indices = jOffice.getJSONArray("officialIndices");
                    int index[] = new int[indices.length()];
                    for (int j = 0; j < indices.length(); j++) {
                        index[j] = Integer.parseInt(String.valueOf(indices.get(j)));
                    }
                    officesList.add(new Offices(name,index));
                }

            }

            for (int i=0;i<officialList.size();i++)
                Log.d(TAG, "parseJSON: Array list (Official)"+officialList.get(i).toString());

            for (int i=0;i<officesList.size();i++)
                Log.d(TAG, "parseJSON: Array list (Offices)"+officialList.get(i).toString());

            for(int k=0;k<officesList.size();k++){

                int indices[] = officesList.get(k).getIndices();

                if(indices.length!=0){
                    for(int i=0;i<indices.length;i++){
                      //  officialPerson.clear();
                        LinkedHashMap<String,Official> hmap= new LinkedHashMap<>();
                        Official off = (officialList.get(indices[i]));
                        hmap.put(officesList.get(k).getName(),off);
                        list.add(hmap);
                    }
                }
            }
    
           for(int i=0;i<list.size();i++)
               Log.d(TAG, "List "+list.get(i).toString());
            return list;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    catch (Exception e) {
            Log.d(TAG, "parseJSaON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
}
}