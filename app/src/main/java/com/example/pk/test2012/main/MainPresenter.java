package com.example.pk.test2012.main;

/**
 * Created by pk on 24.12.2016.
 */
public interface MainPresenter {
    void loadData(String url);

    void itemLongClick(int position);
    void mapBtnClick();

    void filterCardClick();

    void sortCardClick();

    void changeSortSetting(int flag);

}
