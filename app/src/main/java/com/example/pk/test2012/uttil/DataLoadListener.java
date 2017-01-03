package com.example.pk.test2012.uttil;

import com.example.pk.test2012.EarthQuake;

import java.util.ArrayList;

/**
 * Created by pk on 21.12.2016.
 */
public interface DataLoadListener {
    void onLoad(ArrayList<EarthQuake> data);
}
