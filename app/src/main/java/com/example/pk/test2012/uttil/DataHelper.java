package com.example.pk.test2012.uttil;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pk.test2012.EarthQuake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pk on 22.12.2016.
 */
public class DataHelper {
    public void loadDataWithListener(String url, DataLoadListener listener) {
        Log.d("tag", "Start Loadaing data, url " + url);
        ParseTask parseTask = new ParseTask(listener);
        parseTask.execute(url);
    }

    class ParseTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        HttpURLConnection urlConnection = null;
        InputStream inputStream;
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

                inputStream = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[500];
                while (inputStream.available() > 0) {
                    int count = inputStream.read(buffer);
                    baos.write(buffer, 0, count);
                }
                resultJson = baos.toString("UTF-8");
                inputStream.close();
                baos.close();
                buffer = null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tag", e.getMessage() + "\n ERROR");
            }
            urlConnection.disconnect();
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
                int parseCount = features.length();
                if (parseCount > 100) {
                    parseCount = 100;
                }
                for (int i = 0; i < parseCount; i++) {
                    item = features.getJSONObject(i);
                    itemProperties = item.getJSONObject(PROPETRIES);
                    itemGeometry = item.getJSONObject(GEOMETRY);
                    coordinates = itemGeometry.getJSONArray(COORDINATES);
                    longitude = coordinates.getDouble(0);
                    latitude = coordinates.getDouble(1);
                    try {
                        if (itemProperties.getLong(MAGNITUDE) != 0) {
                            arrayList.add(new EarthQuake(itemProperties.getString(PLACE), itemProperties.getLong(MAGNITUDE), itemProperties.getLong(TIME), latitude, longitude));
                        }
                    } catch (JSONException es) {
                    }
                }
                features=null;
                dataJsonObj=null;
                item = null;
                itemProperties=null;
                itemGeometry=null;
            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.d("tag", e1.getMessage());
            }
            return arrayList;
        }
    }
}
