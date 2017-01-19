package com.example.pk.test2012.main;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

/**
 * Created by pk on 24.12.2016.
 */
public interface MainPresenter {
    void loadData(String url,int sortFlag);

    void itemLongClick(int position);

    void mapBtnClick();

    void filterCardClick();

    void sortCardClick();

    void onSortChange(int flag);

    void onScrolledRecyclerView(int dy, LinearLayout bottomTab, boolean isAnimationWorking, LinearLayoutManager lm);

    void onFiltrChange(String newRequestUrl);
}
