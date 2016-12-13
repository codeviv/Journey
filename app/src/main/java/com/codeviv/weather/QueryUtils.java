package com.codeviv.weather;

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

/**
 * Created by vivek on 12/12/2016.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }
    public static URL createURL(String stringUrl) {
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"problem building the URl");
        }
        return url;
    }
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch(IOException e){
            Log.e(LOG_TAG, "Problem retreiving the earthquake Json results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream == null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static ArrayList<Weather> extractFeatureFromJson(String weatherJSON) {
        ArrayList<Weather> weathers = new ArrayList<>();

        if(TextUtils.isEmpty(weatherJSON)) {
            return null;
        }
        try {
            JSONObject root = new JSONObject(weatherJSON);
            JSONObject  city = root.optJSONObject("city");
            String place = city.optString("name");

            JSONArray weatherArray = root.optJSONArray("list");

            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject weatherObject = weatherArray.getJSONObject(i);
                Long date = weatherObject.optLong("dt");

                JSONObject main = weatherObject.optJSONObject("main");
                Double temp = main.optDouble("temp");

                Weather weather = new Weather(place, date, temp);
                weathers.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "problem parsing the earthquake json resuls", e);
        }

        return weathers;

    }
    public static List<Weather> fetchEarthqukeData(String requestUrl) {
        URL url = createURL((requestUrl));

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request");
        }

        List<Weather> weathers = extractFeatureFromJson(jsonResponse);

        return weathers;
    }
}

