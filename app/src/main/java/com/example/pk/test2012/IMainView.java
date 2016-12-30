package com.example.pk.test2012;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public interface IMainView {
    void setItem(ArrayList<EarthQuake> data);

    void showPopupMap(int itemPosition);

    void showDialogFilterSetting();
    void openMapActivity();
}
