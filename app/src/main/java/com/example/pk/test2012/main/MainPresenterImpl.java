package com.example.pk.test2012.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
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
    String requestUrl = "m";
    Context context;
    ArrayList<EarthQuake> mydata;
    int sortFlag;

    RecyclerViewAdapter adapter;

    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

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
                if (requestUrl == null) {
                    requestUrl = Constants.DEFAULT_URL_REQUEST;
                    dataHelper.loadDataWithListener(requestUrl, this);
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
        RecyclerViewAdapter.clearData();
        SortTask sortTask = new SortTask(adapter, mydata);
        sortTask.execute(sortFlag);
    }


    @Override
    public void onScrolledRecyclerView(int dy, LinearLayout bottomTab, boolean isAnimationWorking, LinearLayoutManager lm) {
        if (lm.findLastVisibleItemPosition() > adapter.COUNT_VISIBLY_ITEMS * 0.85) {
            adapter.insertNextItems();
        }
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
        mainView.hideProgress();
        onSortChange(sortFlag);
    }
}
