package com.example.pk.test2012.uttil;

import android.util.Log;

/**
 * Created by pk on 22.12.2016.
 */
public class DataHelper {
    public void loadDataWithListener(String url, DataLoadListener listener) {
        Log.d("tag", "Start Loadaing data, url " + url);
        ParseTask parseTask = new ParseTask(listener);
        parseTask.execute(url);
    }
}
