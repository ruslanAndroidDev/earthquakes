package com.example.pk.test2012.main;

import android.os.AsyncTask;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DataListener;

import java.util.ArrayList;

/**
 * Created by pk on 19.01.2017.
 */
public class SortTask extends AsyncTask<Integer, EarthQuake, ArrayList<EarthQuake>> {
    ArrayList<EarthQuake> data;
    int size;
    DataListener.DataSorting listener;

    public SortTask(ArrayList<EarthQuake> mydata, DataListener.DataSorting listener) {
        this.listener = listener;
        data = mydata;
    }

    @Override
    protected ArrayList<EarthQuake> doInBackground(Integer... params) {
        size = data.size();
        int flag = params[0];
        if (flag == Constants.Sort_FLAG_DATE) {
            sortDate();
        } else if (flag == Constants.SORT_FLAG_POWERFUL_FIRST) {
            sortPowerfulFirst();
        } else if (flag == Constants.SORT_FLAG_WEAK_FIRST) {
            sortWeakFirst();
        }
        return data;
    }

    private void sortDate() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getTime() < data.get(k + 1).getTime()) {
                    swapitem(k);
                }
            }
        }
    }

    private void sortWeakFirst() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getMagnitude() > data.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
        }
    }

    private void swapitem(int k) {
        EarthQuake quake = data.get(k);
        data.set(k, data.get(k + 1));
        data.set(k + 1, quake);
    }

    private void sortPowerfulFirst() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size - i - 1; k++) {
                if (data.get(k).getMagnitude() < data.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
        }
    }


    @Override
    protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
        super.onPostExecute(earthQuakes);
        listener.onSorted(earthQuakes);
    }
}
