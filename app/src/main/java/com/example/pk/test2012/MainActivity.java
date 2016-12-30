package com.example.pk.test2012;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pk.test2012.uttil.Constants;
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

public class MainActivity extends AppCompatActivity implements IMainView, RecyclerEvent.LongClickListener, View.OnClickListener, DialogListener {
    RecyclerView recyclerView;
    LongPressPopup mapPopup;
    ArrayList<EarthQuake> listItem;
    int popup_position;
    SupportMapFragment mapFragment;
    GoogleMap gMap;
    MainPresenterImpl presenter;
    LinearLayout bottomTab;
    ImageView mapBtn;

    CardView cardSort;
    CardView cardFiltr;

    boolean animationStart;
    FiltrDialogFragment dialog;
    SharedPreferences sPref;
    String url;
    LoadingView loadingView;
    Animation anim_out;
    Animation anim_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPref = getPreferences(MODE_PRIVATE);
        url = sPref.getString(Constants.SHAREDPREF_KEY_URL, Constants.DEFAULT_URL_REQUEST);
        anim_in = AnimationUtils.loadAnimation(this, R.anim.translate_in);
        anim_out = AnimationUtils.loadAnimation(this, R.anim.translate_out);
        dialog = new FiltrDialogFragment(this);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        cardSort = (CardView) findViewById(R.id.card_sort);
        cardSort.setOnClickListener(this);
        cardFiltr = (CardView) findViewById(R.id.card_filtr);
        cardFiltr.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bottomTab = (LinearLayout) findViewById(R.id.bottom_tab);
        bottomTab.setOnClickListener(this);
        mapBtn = (ImageView) findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new MainPresenterImpl(this);
        presenter.loadData(url);
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationStart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomTab.setVisibility(View.VISIBLE);
                animationStart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationStart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomTab.setVisibility(View.GONE);
                animationStart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("tag", dy + "");
                if (dy > 0) {
                    if ((bottomTab.getVisibility() == View.VISIBLE) & (animationStart == false)) {
                        bottomTab.startAnimation(anim_out);
                    }
                } else {
                    if ((bottomTab.getVisibility() == View.GONE) & (animationStart == false)) {
                        bottomTab.startAnimation(anim_in);
                    }
                }
            }
        });
    }

    @Override
    public void onLongClick(int position) {
        presenter.itemLongClick(position);
        popup_position = position;
    }

    @Override
    public void setItem(ArrayList<EarthQuake> data) {
        loadingView.stop();
        loadingView.setVisibility(View.GONE);
        listItem = data;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(data, this, this));
    }

    private LongPressPopup createPopup() {
        LongPressPopupBuilder builder = new LongPressPopupBuilder(this);
        builder.setPopupView(R.layout.popup, new PopupInflaterListener() {
            @Override
            public void onViewInflated(@Nullable String popupTag, View root) {
                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
            }
        });
        builder.setPopupListener(new PopupStateListener() {
            @Override
            public void onPopupShow(@Nullable String popupTag) {
                if (gMap == null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            gMap = googleMap;
                            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            showPopupMap(popup_position);
                        }
                    });
                } else {
                    showPopupMap(popup_position);
                }
            }

            @Override
            public void onPopupDismiss(@Nullable String popupTag) {
                gMap.clear();
            }
        });
        builder.setDismissOnLongPressStop(false)
                .setDismissOnTouchOutside(true)
                .setDismissOnBackPressed(true)
                .setTarget(new TextView(this));
        return mapPopup = builder.build();
    }

    @Override
    public void showPopupMap(int itemPosition) {
        if (mapPopup == null) {
            createPopup();
        }
        mapPopup.showNow();
        EarthQuake currentEarthQuake = listItem.get(itemPosition);
        LatLng coordinates = new LatLng(currentEarthQuake.getLatitude(), currentEarthQuake.getLongitude());
        int circleColor = Utiil.calculateCircleColor(this, currentEarthQuake.getMagnitude());
        gMap.addCircle(new CircleOptions().center(coordinates).radius(14000).fillColor(circleColor).strokeColor(Color.TRANSPARENT));
        gMap.addCircle(new CircleOptions().center(coordinates)
                .radius(24000).strokeColor(circleColor).strokeWidth(10));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 7));

    }

    @Override
    public void showDialogFilterSetting() {
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.map_btn) {
            presenter.mapBtnClick();
        } else if (v.getId() == R.id.card_filtr) {
            Log.d("tag", "clickOnCardFiltr");
            presenter.filterCardClick();
        } else if (v.getId() == R.id.card_sort) {

        }
    }

    @Override
    public void OnFiltrChange(String newRequestUrl) {
        if (newRequestUrl != url) {
            presenter.loadData(newRequestUrl);
            url = newRequestUrl;
        }
    }
}
