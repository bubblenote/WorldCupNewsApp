package com.example.emilykuo.worldcupnewsapp;


import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final int SUCCESSFUL_RESPONSE = 200;
    public static final int MAX_READ_TIMEOUT = 1000;
    public static final int MAX_CONNECT_TIMEOUT = 15000;

    public QueryUtils() {

    }

    private static URL makeURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException error) {
            Log.e(LOG_TAG, "Problem Making the URL", error);
        }
        return url;
    }

    private static String makeHTTPrequest(URL url) throws IOException {
        String jsonResonse = "";

        if (url == null) {
            return jsonResonse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(MAX_READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(MAX_CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == SUCCESSFUL_RESPONSE) {

                inputStream = httpURLConnection.getInputStream();
                jsonResonse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error Response Code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with making the connection ", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResonse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();

    }

    private static List<Story> extractJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        ArrayList<Story> stories = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(json);
            JSONObject object = obj.getJSONObject("response");
            JSONArray array = object.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {

                JSONObject currentObj = array.getJSONObject(i);
                String title = currentObj.getString("webTitle");
                String section = currentObj.getString("sectionName");
                String date = currentObj.getString("webPublicationDate");
                String url = currentObj.getString("webUrl");

                JSONArray tagArray = currentObj.getJSONArray("tags");
                if (tagArray != null && tagArray.length()>0) {
                    JSONObject authorObj = tagArray.getJSONObject(0);
                    String author = authorObj.getString("webTitle");
                    Story str = new Story(title, author, section, date, url);
                    stories.add(str);
                }
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing JSON results", e);
        }
        return stories;

    }

    public static List<Story> extractStories(String stringUrl) {
        URL url = makeURL(stringUrl);
        String json = null;
        try {
            json = makeHTTPrequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with HTTP request", e);
        }

        List<Story> stories = extractJson(json);
        return stories;
    }

}
