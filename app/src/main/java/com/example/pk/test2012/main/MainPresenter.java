package com.example.pk.test2012.main;

import android.widget.LinearLayout;

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

    void onScrolledRecyclerView(int dy, LinearLayout bottomTab, boolean isAnimationWorking);

    void onFiltrChange(String newRequestUrl);
}
