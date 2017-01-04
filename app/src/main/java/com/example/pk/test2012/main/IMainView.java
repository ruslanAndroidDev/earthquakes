package com.example.pk.test2012.main;

import com.example.pk.test2012.EarthQuake;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public interface IMainView {
    void setItem(ArrayList<EarthQuake> data);

    void showPopupMap(int itemPosition);

    void showBottomSheetFilter();
    void openMapActivity();

    void showBottomSheetSort();

    void showBottomTab();
    void hideBottomTab();
}
