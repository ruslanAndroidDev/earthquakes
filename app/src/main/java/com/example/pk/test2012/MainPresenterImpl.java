package com.example.pk.test2012;

import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DataHelper;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public class MainPresenterImpl implements DataLoadListener, MainPresenter {
    IMainView mainView;

    public MainPresenterImpl(IMainView mainView) {
        this.mainView = mainView;
    }

    public void loadData(String url) {
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
        mainView.showDialogFilterSetting();
    }

    @Override
    public void onLoad(ArrayList<EarthQuake> data) {
        mainView.setItem(data);
    }
}
