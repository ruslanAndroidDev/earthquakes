package com.example.pk.test2012.main;

import com.example.pk.test2012.EarthQuake;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public interface IMainView {
    void setItem(ArrayList<EarthQuake> data);

    void showPopupMap(int itemPosition);

    void showProgress();

    void hideProgress();

    void showBottomSheetFilter();

    void showBottomSheetSort();

    void openMapActivity(String url);

    void showBottomTab();

    void hideBottomTab();

    void showOffLineMessage();
}
