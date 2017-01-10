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
    ArrayList<EarthQuake> mydata;
    int sortFlag;

    public MainPresenterImpl(IMainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        dataHelper = new DataHelper();
    }

    DataHelper dataHelper;

    public void loadData(String newUrl, int sort_flag) {
        if (sort_flag != -1) {
            sortFlag = sort_flag;
        }
        if (isNetworkConection()) {
            if (newUrl.equals("")) {
                if (requestUrl.equals("")) {
                    newUrl = Constants.DEFAULT_URL_REQUEST;
                    dataHelper.loadDataWithListener(newUrl, this);
                    requestUrl = newUrl;
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
    }

    @Override
    public void sortCardClick() {
        mainView.showBottomSheetSort();
    }

    @Override
    public void onSortChange(int flag) {
        sortFlag = flag;
        switch (flag) {
            case Constants.Sort_FLAG_DATE:
                sortDate();
                break;
            case Constants.SORT_FLAG_POWERFUL_FIRST:
                sortPowerfulFirst();
                break;
            case Constants.SORT_FLAG_WEAK_FIRST:
                sortWeakFirst();
                break;
        }
    }

    private void sortDate() {
        for (int i = 0; i < mydata.size(); i++) {
            for (int k = 0; k < mydata.size() - i - 1; k++) {
                if (mydata.get(k).getTime() < mydata.get(k + 1).getTime()) {
                    swapitem(k);
                }
            }
        }
        mainView.setItem(mydata);
    }

    private void sortWeakFirst() {
        for (int i = 0; i < mydata.size(); i++) {
            for (int k = 0; k < mydata.size() - i - 1; k++) {
                if (mydata.get(k).getMagnitude() > mydata.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
        }
        mainView.setItem(mydata);
    }

    private void swapitem(int k) {
        EarthQuake quake = mydata.get(k);
        mydata.set(k, mydata.get(k + 1));
        mydata.set(k + 1, quake);
    }

    private void sortPowerfulFirst() {
        for (int i = 0; i < mydata.size(); i++) {
            for (int k = 0; k < mydata.size() - i - 1; k++) {
                if (mydata.get(k).getMagnitude() < mydata.get(k + 1).getMagnitude()) {
                    swapitem(k);
                }
            }
        }
        mainView.setItem(mydata);
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
            dataHelper.loadDataWithListener(newRequestUrl, this);
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
        mydata = data;
        onSortChange(sortFlag);
        mainView.hideProgress();
        mainView.setItem(mydata);
    }
}
