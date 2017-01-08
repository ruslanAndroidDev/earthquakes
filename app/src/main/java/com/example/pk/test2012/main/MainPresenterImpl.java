package com.example.pk.test2012.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DataHelper;
import com.example.pk.test2012.uttil.DataLoadListener;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public class MainPresenterImpl implements DataLoadListener, MainPresenter {
    IMainView mainView;
    String requestUrl;
    Context context;

    public MainPresenterImpl(IMainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    DataHelper dataHelper;

    public void loadData(String newUrl) {
        if (dataHelper == null) {
            dataHelper = new DataHelper();
        }
        if (isNetworkConection()) {
            if (newUrl.equals("")) {
                if (requestUrl == null) {
                    newUrl = Constants.DEFAULT_URL_REQUEST;
                    dataHelper.loadDataWithListener(newUrl, this);
                } else {
                    dataHelper.loadDataWithListener(requestUrl, this);
                }
            } else if (!newUrl.equals(requestUrl)) {
                requestUrl = newUrl;
                dataHelper.loadDataWithListener(newUrl, this);
            }
        } else {
            mainView.showOffLineMessage();
        }
    }

    @Override
    public void itemLongClick(int position) {
        mainView.hideBottomTab();
        mainView.showPopupMap(position);
    }

    @Override
    public void mapBtnClick() {
        if (!isNetworkConection()) {
            Toast.makeText(context, "No internet Conection", Toast.LENGTH_LONG).show();
        } else {
            mainView.openMapActivity(requestUrl);
        }
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
    public void onSortChange(int flag) {
        switch (flag) {
            case Constants.Sort_FLAG_DATE:
                break;
            case Constants.SORT_FLAG_POWERFUL_FIRST:
                break;
            case Constants.SORT_FLAG_WEAK_FIRST:
                break;
        }
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
        if (!newRequestUrl.equals(requestUrl)) {
            mainView.showProgress();
            RecyclerViewAdapter.clearData();
            loadData(newRequestUrl);
            requestUrl = newRequestUrl;
        }
    }

    private boolean isNetworkConection() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onLoad(ArrayList<EarthQuake> data) {
        mainView.hideProgress();
        mainView.setItem(data);
        mainView.showBottomTab();
    }
}
