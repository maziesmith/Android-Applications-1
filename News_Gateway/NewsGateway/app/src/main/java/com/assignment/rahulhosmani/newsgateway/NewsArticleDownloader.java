package com.assignment.rahulhosmani.newsgateway;

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

public class NewsArticleDownloader extends AsyncTask<String, Integer, String> {

    private final String URL_ARTICLE_HEAD = "https://newsapi.org/v2/top-headlines?pageSize=50&sources=";
    private final String URL_ARTICLE_TAIL = "&apiKey=";
    private final String KEY = "162e30f6008e4d56a491c86a19408d97";
    private static final String TAG = "NewsArticleDownloader";
    static NewsService newsService = new NewsService();

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Article> articles_data = parseJSON(s);
        newsService.setArticles(articles_data);
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri;
        dataUri = Uri.parse(URL_ARTICLE_HEAD + params[0].toLowerCase() + URL_ARTICLE_TAIL + KEY);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int HTTP_NOT_FOUND = conn.getResponseCode();
            if (HTTP_NOT_FOUND == 404) {
                return null;
            } else {
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return sb.toString();

            }
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Article> parseJSON(String s) {
        try {
            Log.d(TAG, "parseJSON");
            ArrayList<Article> articalList = new ArrayList<>();
            String author = null, title = null, description = null, urlToImage = null, publishedAt = null, news_url = null;
            JSONObject jObjMain = new JSONObject(s);

            JSONArray articles = jObjMain.getJSONArray("articles");
            Log.d(TAG, "Length " + articles.length());
            for (int i = 0; i < articles.length(); i++) {
                JSONObject source_object = (JSONObject) articles.get(i);
                author = source_object.getString("author");
                title = source_object.getString("title");
                description = source_object.getString("description");
                urlToImage = source_object.getString("urlToImage");
                publishedAt = source_object.getString("publishedAt");
                news_url = source_object.getString("url");
                articalList.add(new Article(author, title, description, urlToImage, publishedAt, news_url));
            }
            return articalList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

