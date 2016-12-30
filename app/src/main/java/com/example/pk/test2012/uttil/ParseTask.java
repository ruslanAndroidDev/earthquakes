package com.example.pk.test2012.uttil;


import android.os.AsyncTask;
import android.util.Log;

import com.example.pk.test2012.DataLoadListener;
import com.example.pk.test2012.EarthQuake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pk on 21.12.2016.
 */

class ParseTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";
    URL url;
    DataLoadListener listener;

    public ParseTask(DataLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<EarthQuake> doInBackground(String... params) {

        return parseJson(getJsonFromUrl(params[0]));

    }

    @Override
    protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
        super.onPostExecute(earthQuakes);
        listener.onLoad(earthQuakes);
    }


    private String getJsonFromUrl(String urlStr) {
        try {
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
            Log.d("tag", "json " + resultJson);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", e.getMessage() + "\n ERROR");
        }
        return resultJson;
    }

    JSONObject dataJsonObj;
    JSONArray features;

    ArrayList<EarthQuake> parseJson(String json) {
        ArrayList<EarthQuake> arrayList = new ArrayList<>();
        try {
            dataJsonObj = new JSONObject(json);
            features = dataJsonObj.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject item = features.getJSONObject(i);
                JSONObject itemProperties = item.getJSONObject("properties");
                JSONObject itemGeometry = item.getJSONObject("geometry");
                JSONArray coordinates = itemGeometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);
                EarthQuake earthQuake = new EarthQuake(itemProperties.getString("place"), itemProperties.getDouble("mag"), itemProperties.getLong("time"), itemProperties.getString("url"), latitude, longitude);
                arrayList.add(earthQuake);
            }
            Log.d("tag", "arrayList.size()" + arrayList.size());
        } catch (JSONException e1) {
            e1.printStackTrace();
            Log.d("tag", e1.getMessage());
        }
        return arrayList;
    }
}
