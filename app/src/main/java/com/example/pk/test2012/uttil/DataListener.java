package com.example.pk.test2012.uttil;

import com.example.pk.test2012.EarthQuake;

import java.util.ArrayList;

/**
 * Created by pk on 21.12.2016.
 */
public interface DataListener {
    interface DataLoading {
        void onLoad(ArrayList<EarthQuake> data);
    }
    interface DataSorting {
        void onSorted(ArrayList<EarthQuake> sortedData);
    }
}
