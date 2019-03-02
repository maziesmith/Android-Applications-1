package com.assignment.rahulhosmani.stockwatch;

import android.graphics.Bitmap;
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
import java.util.HashMap;

public class NameDownloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "NameDownloader";
    private MainActivity mainActivity;
    private HashMap<String, String> stockData = new HashMap<>();
    private final String STOCK_SYMBOLS_URL = "https://api.iextrading.com/1.0/ref-data/symbols";


    public NameDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        Uri.Builder buildURL = Uri.parse(STOCK_SYMBOLS_URL).buildUpon();
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "doInBackground: Response Code: " + conn.getResponseCode() + ", " + conn.getResponseMessage());

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

        parseJSONData(sb.toString());
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mainActivity.getStockSymbolData(stockData);
    }

    private void parseJSONData(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stockData.put(jsonObject.getString("symbol"), jsonObject.getString("name"));
            }
            Log.d(TAG, "parseJSON: stock data parsed to hash map " + stockData.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
