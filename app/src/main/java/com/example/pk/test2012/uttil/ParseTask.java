package com.example.pk.test2012.uttil;


import android.os.AsyncTask;
import android.util.Log;

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
    String PROPETRIES = "properties";
    String GEOMETRY = "geometry";
    String COORDINATES = "coordinates";
    String MAGNITUDE = "mag";
    String PLACE = "place";
    String FEATURES = "features";
    String TIME = "time";

    public ParseTask(DataLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<EarthQuake> doInBackground(String... params) {
        Log.d("tag,", getJsonFromUrl(params[0]));
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
            reader.close();
            inputStream.close();
            urlConnection.disconnect();
            reader = null;
            buffer = null;
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
            features = dataJsonObj.getJSONArray(FEATURES);
            JSONObject item;
            JSONObject itemProperties;
            JSONObject itemGeometry;
            JSONArray coordinates;
            double longitude;
            double latitude;
            for (int i = 0; i < features.length(); i++) {
                item = features.getJSONObject(i);
                itemProperties = item.getJSONObject(PROPETRIES);
                itemGeometry = item.getJSONObject(GEOMETRY);
                coordinates = itemGeometry.getJSONArray(COORDINATES);
                longitude = coordinates.getDouble(0);
                latitude = coordinates.getDouble(1);
                try {
                    arrayList.add(new EarthQuake(itemProperties.getString(PLACE), itemProperties.getLong(MAGNITUDE), itemProperties.getLong(TIME), latitude, longitude));
                } catch (JSONException es) {
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
            Log.d("tag", e1.getMessage());
        }
        return arrayList;
    }
}
