package com.example.pk.test2012.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pk.test2012.EarthQuake;
import com.example.pk.test2012.MapActivity;
import com.example.pk.test2012.MyBottomSheetFiltr;
import com.example.pk.test2012.R;
import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DialogListener;
import com.example.pk.test2012.uttil.Utiil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import me.wangyuwei.loadingview.LoadingView;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupStateListener;

public class MainActivity extends AppCompatActivity implements IMainView, RecyclerEvent.LongClickListener, View.OnClickListener, DialogListener.FiltrDialogListener, DialogListener.SortDialogListener, PopupInflaterListener, PopupStateListener {
    RecyclerView recyclerView;
    MainPresenterImpl presenter;
    LinearLayout bottomTab;
    ImageView toolbarMapBtn;

    CardView cardSort;
    CardView cardFiltr;
    int popup_position;

    boolean isAnimationWorking;
    SharedPreferences sPref;
    LoadingView loadingView;
    Animation anim_out, anim_in;

    String requestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                sPref = getPreferences(MODE_PRIVATE);
                requestUrl = sPref.getString(Constants.SHAREDPREF_KEY_URL, Constants.DEFAULT_URL_REQUEST);
            }
        });
        t.start();
        cardSort = (CardView) findViewById(R.id.card_sort);
        cardSort.setOnClickListener(this);
        cardFiltr = (CardView) findViewById(R.id.card_filtr);
        cardFiltr.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomTab = (LinearLayout) findViewById(R.id.bottom_tab);

        toolbarMapBtn = (ImageView) findViewById(R.id.map_btn);
        toolbarMapBtn.setOnClickListener(this);

        loadingView = (LoadingView) findViewById(R.id.loadingView);
        loadingView.start();

        presenter = new MainPresenterImpl(this);
        presenter.loadData(requestUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        anim_in = AnimationUtils.loadAnimation(this, R.anim.translate_in);
        anim_out = AnimationUtils.loadAnimation(this, R.anim.translate_out);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolledRecyclerView(dy, bottomTab, isAnimationWorking);
            }
        });
    }

    public void showBottomTab() {
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationWorking = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomTab.setVisibility(View.VISIBLE);
                isAnimationWorking = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bottomTab.startAnimation(anim_in);
    }

    public void hideBottomTab() {
        bottomTab.startAnimation(anim_out);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationWorking = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomTab.setVisibility(View.GONE);
                isAnimationWorking = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public void onLongClickRecyclerView(int position) {
        presenter.itemLongClick(position);
        popup_position = position;
    }

    @Override
    public void setItem(ArrayList<EarthQuake> data) {
        loadingView.stop();
        recyclerView.setAdapter(new RecyclerViewAdapter(data, this, this));
        showBottomTab();
    }

    LongPressPopup mapPopup;

    private void createMapPopup() {
        mapPopup = new LongPressPopupBuilder(this)
                .setPopupView(R.layout.popup, this)
                .setPopupListener(this)
                .setDismissOnLongPressStop(false)
                .setDismissOnTouchOutside(true)
                .setDismissOnBackPressed(true)
                .setTarget(new TextView(this))
                .build();
    }

    @Override
    public void showPopupMap(int itemPosition) {
        if (mapPopup == null) {
            createMapPopup();
        }
        mapPopup.showNow();
    }

    MyBottomSheetFiltr mBottomSheet;

    @Override
    public void showBottomSheetFilter() {
        if (mBottomSheet == null) {
            mBottomSheet = new MyBottomSheetFiltr(this);
        }
        mBottomSheet.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("url", requestUrl);
        startActivity(intent);
    }

    @Override
    public void showBottomSheetSort() {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.map_btn) {
            presenter.mapBtnClick();
        } else if (v.getId() == R.id.card_filtr) {
            presenter.filterCardClick();
        } else if (v.getId() == R.id.card_sort) {
            presenter.sortCardClick();
        }
    }

    @Override
    public void OnFiltrChange(String newRequestUrl) {
        presenter.onFiltrChange(newRequestUrl);
    }

    @Override
    public void OnSortChange(int flag) {
        presenter.changeSortSetting(flag);
    }

    SupportMapFragment mapFragment;

    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
    }

    GoogleMap gMap;

    @Override
    public void onPopupShow(@Nullable String popupTag) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                EarthQuake currentEarthQuake = RecyclerViewAdapter.earthQuakesdata.get(popup_position);
                LatLng coordinates = new LatLng(currentEarthQuake.getLatitude(), currentEarthQuake.getLongitude());
                int circleColor = Utiil.calculateCircleColor(getApplicationContext(), currentEarthQuake.getMagnitude());
                //internal circle
                googleMap.addCircle(new CircleOptions().center(coordinates).radius(14000).fillColor(circleColor).strokeColor(Color.TRANSPARENT));
                //external circle
                googleMap.addCircle(new CircleOptions().center(coordinates)
                        .radius(24000).strokeColor(circleColor).strokeWidth(10));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 7));
            }
        });
    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {
        gMap.clear();
    }
}