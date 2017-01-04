package com.example.pk.test2012.main;

import android.view.View;
import android.widget.LinearLayout;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.uttil.DataHelper;
import com.example.pk.test2012.uttil.DataLoadListener;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public class MainPresenterImpl implements DataLoadListener, MainPresenter {
    IMainView mainView;
    String requestUrl;

    public MainPresenterImpl(IMainView mainView) {
        this.mainView = mainView;
    }

    public void loadData(String url) {
        requestUrl = url;
        DataHelper dataHelper = new DataHelper();
        dataHelper.loadDataWithListener(url, this);
    }

    @Override
    public void itemLongClick(int position) {
        mainView.showPopupMap(position);
    }

    @Override
    public void mapBtnClick() {
        mainView.openMapActivity();
    }

    @Override
    public void filterCardClick() {
        mainView.showBottomSheetFilter();
        mainView.hideBottomTab();
    }

    @Override
    public void sortCardClick() {
        mainView.showBottomSheetSort();
        mainView.hideBottomTab();
    }

    @Override
    public void changeSortSetting(int flag) {
        mainView.showBottomTab();

    }

    @Override
    public void onScrolledRecyclerView(int dy, LinearLayout bottomTab, boolean isAnimationWorking) {
        if (dy > 0) {
            if ((bottomTab.getVisibility() == View.VISIBLE) & (isAnimationWorking == false)) {
                mainView.hideBottomTab();
            }
        } else {
            if ((bottomTab.getVisibility() == View.GONE) & (isAnimationWorking == false)) {
                mainView.showBottomTab();
            }
        }
    }

    @Override
    public void onFiltrChange(String newRequestUrl) {
        if (newRequestUrl != requestUrl) {
            loadData(newRequestUrl);
            requestUrl = newRequestUrl;
        }
        mainView.showBottomTab();
    }

    @Override
    public void onLoad(ArrayList<EarthQuake> data) {
        mainView.setItem(data);
    }
}
