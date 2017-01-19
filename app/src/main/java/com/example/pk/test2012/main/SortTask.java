package com.example.pk.test2012.main;

import android.os.AsyncTask;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.uttil.Constants;

import java.util.ArrayList;

/**
 * Created by pk on 19.01.2017.
 */
public class SortTask extends AsyncTask<Integer, EarthQuake, Void> {
    int flag;
    ArrayList<EarthQuake> data;
    RecyclerViewAdapter adapter;
    int size;

    public SortTask(RecyclerViewAdapter adapter, ArrayList<EarthQuake> mydata) {
        this.adapter=adapter;
        data = mydata;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        size = data.size();
        int flag = params[0];
        if (flag== Constants.Sort_FLAG_DATE) {
            sortDate();
        } else if (flag == Constants.SORT_FLAG_POWERFUL_FIRST) {
            sortPowerfulFirst();
        } else if (flag == Constants.SORT_FLAG_WEAK_FIRST) {
            sortWeakFirst();
        }
        return null;
    }

    private void sortDate() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getTime() > data.get(k + 1).getTime()) {
                    swapitem(k);
                }
            }
            publishProgress(data.get(size-1-i));
        }
    }

    private void sortWeakFirst() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getMagnitude() < data.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
            publishProgress(data.get(size-1-i));
        }
    }

    private void swapitem(int k) {
        EarthQuake quake = data.get(k);
        data.set(k, data.get(k + 1));
        data.set(k + 1, quake);
    }

    private void sortPowerfulFirst() {
        int size = data.size();
        for (int i = 0; i <size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getMagnitude() > data.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
            publishProgress(data.get(size-1-i));
        }
    }

    @Override
    protected void onProgressUpdate(EarthQuake... values) {
        super.onProgressUpdate(values);
        adapter.addItem(values[0]);
    }
}
