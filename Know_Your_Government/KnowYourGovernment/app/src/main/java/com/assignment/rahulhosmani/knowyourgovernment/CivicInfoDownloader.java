package com.assignment.rahulhosmani.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CivicInfoDownloader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private String address;
    private String key;
    private static String DEFAULT_STRING = "No Data Provided";
    private String locCity_State_Zip;
    public List<Official> officialsObjList = new ArrayList<Official>();

    private static final String TAG = "CivicInfoDownloader";
    private String URI1 = "https://www.googleapis.com/civicinfo/v2/representatives?key=";

    public CivicInfoDownloader(MainActivity mainActivity, String address, String key) {
        this.mainActivity = mainActivity;
        this.address = address;
        address = address.trim();
        address = address.replace(",","%2C");
        address = address.replace(" ","%20");
        this.key = key;
        URI1 += key + "&address=\"" + address + "\"";
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String str) {
        mainActivity.officialsObjList.clear();
        if(str == null){
            locCity_State_Zip = "No Data For Location";
        }else {
            parseJSON(str);
        }
        Object[] obj = new Object[2];
        obj[0] = officialsObjList;
        obj[1] = locCity_State_Zip;
        mainActivity.updateList(obj);
    }

    @Override
    protected String doInBackground(String... strings) {
        Uri getDataURI = Uri.parse(URI1);
        String finalURL = getDataURI.toString();

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(finalURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch(Exception e) {
            Log.d(TAG, "doInBackgroundError: " + e.toString());
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());
        return sb.toString();
    }

    void parseJSON (String str){
        List<String> officeName = new ArrayList<>();
        try {
            JSONObject jsonData = new JSONObject(str);

            JSONObject jsonLocation = jsonData.getJSONObject("normalizedInput");
            JSONArray jsonOffice = jsonData.getJSONArray("offices");
            JSONArray jsonOfficial = jsonData.getJSONArray("officials");

            if(jsonLocation.has("city")){
                if(!jsonLocation.getString("city").equalsIgnoreCase(""))
                    locCity_State_Zip = jsonLocation.getString("city") + ", ";
                else
                    locCity_State_Zip = "";
            }
            if (jsonLocation.has("state")){
                locCity_State_Zip += jsonLocation.getString("state") + " ";
            }
            if(jsonLocation.has("zip")){
                locCity_State_Zip += jsonLocation.getString("zip");
            }

            for (int i = 0; i < jsonOffice.length(); i++) {
                JSONObject c = jsonOffice.getJSONObject(i);
                JSONArray oi = c.getJSONArray("officialIndices");

                //List<Integer> officialIndices = new ArrayList<>();
                for(int j = 0; j < oi.length(); j++){
                    officeName.add(c.getString("name"));
                }

            }
            for (int i = 0; i < jsonOfficial.length(); i++) {
                JSONObject c = jsonOfficial.getJSONObject(i);
                String name, pAddress = "", city = "", state = "", zip = "", partyName = "", phoneNo = "", urls = "", email = "", photoUrl = "";
                JSONArray jArray;
                Map<String, String> channels = new HashMap<>();

                name = c.getString("name");
                if(c.has("address"))
                {
                    jArray = c.getJSONArray("address");
                    JSONObject cAddress = jArray.getJSONObject(0);
                    if(cAddress.has("line1"))
                        pAddress += cAddress.getString("line1") + " ";
                    if(cAddress.has("line2"))
                        pAddress += cAddress.getString("line2") + " ";
                    if(cAddress.has("line3"))
                        pAddress += cAddress.getString("line3") + " ";
                    if(cAddress.has("city"))
                        city = cAddress.getString("city");
                    if(cAddress.has("state"))
                        state = cAddress.getString("state");
                    if(cAddress.has("zip"))
                        zip = cAddress.getString("zip");
                } else {
                    pAddress = DEFAULT_STRING;
                }

                if(c.has("party"))
                    partyName = c.getString("party");
                else
                    partyName = "Unknown";

                if(c.has("phones")) {
                    jArray = c.getJSONArray("phones");
                    phoneNo  = jArray.get(0).toString();
                }
                else
                    phoneNo = DEFAULT_STRING;

                if(c.has("urls")) {
                    jArray = c.getJSONArray("urls");
                    urls  = jArray.get(0).toString();
                }
                else
                    urls = DEFAULT_STRING;

                if(c.has("emails")) {
                    jArray = c.getJSONArray("emails");
                    email  = jArray.get(0).toString();
                }
                else
                    email = DEFAULT_STRING;
                if(c.has("photoUrl")) {
                    photoUrl = c.getString("photoUrl");
                }
                else
                    photoUrl = DEFAULT_STRING;


                if(c.has("channels")) {
                    jArray = c.getJSONArray("channels");
                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject cObj = jArray.getJSONObject(j);
                        channels.put(cObj.getString("type"), cObj.getString("id"));
                    }
                }

                officialsObjList.add(new Official(name, pAddress, city, state, zip, partyName, phoneNo, urls, email, photoUrl,officeName.get(i),channels));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
