package com.example.pk.test2012.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.uttil.DataHelper;
import com.example.pk.test2012.uttil.DataListener;

import java.util.ArrayList;

/**
 * Created by pk on 24.12.2016.
 */
public class MainPresenterImpl implements DataListener.DataLoading, DataListener.DataSorting, MainPresenter {
    IMainView mainView;
    String requestUrl = "m";
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
                dataHelper.loadDataWithListener(requestUrl, this);
            } else {
                dataHelper.loadDataWithListener(newUrl, this);
                requestUrl = newUrl;
            }
        } else {
            mainView.showOffLineMessage();
        }
    }

    @Override
    public void itemLongClick(int position) {
        if (!isNetworkConection()) {
            Toast.makeText(context, "No internet Conection", Toast.LENGTH_LONG).show();
        } else {
            mainView.showPopupMap(position);
        }
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
        SortTask sortTask = new SortTask(mydata, this);
        sortTask.execute(flag);
        sortFlag = flag;
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
    }

    @Override
    public void onSorted(ArrayList<EarthQuake> sortedData) {
        mainView.hideProgress();
        mainView.setItem(sortedData);
    }
}
