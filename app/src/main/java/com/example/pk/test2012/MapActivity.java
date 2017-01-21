package com.example.pk.test2012;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DataHelper;
import com.example.pk.test2012.uttil.DataListener;
import com.example.pk.test2012.uttil.Utiil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DataListener.DataLoading {
    GoogleMap map;
    SupportMapFragment mapFrag;
    Intent urlIntent;
    ArrayList<CircleOptions> circleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        urlIntent = getIntent();
        String url = urlIntent.getStringExtra("url");
        new DataHelper().loadDataWithListener(url, (DataListener.DataLoading) this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapactivity_map);
        mapFrag.getMapAsync(this);
        circleArray = new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(49, 31)));
    }

    @Override
    public void onLoad(ArrayList<EarthQuake> data) {
        Log.d("tag", "Прийшли дані");
        CircleOptions circleInternal = null;
        CircleOptions circleExternal = null;
        for (EarthQuake earthQuake : data) {
            LatLng coordinate = new LatLng(earthQuake.getLatitude(), earthQuake.getLongitude());
            int circleColor = Utiil.calculateCircleColor(this, earthQuake.getMagnitude());
            int size = Utiil.calculateCircleSize(earthQuake.getMagnitude());
            if (size == Constants.SIZE_MIN || size == Constants.SIZE_MEDIUM) {
                circleInternal = new CircleOptions().center(coordinate).radius(earthQuake.getMagnitude() * 40000).fillColor(circleColor).strokeColor(Color.TRANSPARENT);
                circleArray.add(circleInternal);
            }
            if (size == Constants.SIZE_MAX) {
                circleInternal = new CircleOptions().center(coordinate).radius(earthQuake.getMagnitude() * 45000).fillColor(circleColor).strokeColor(Color.TRANSPARENT);
//                circleExternal = new CircleOptions().center(coordinate)
//                        .radius(earthQuake.getMagnitude() * 80000).strokeColor(circleColor).strokeWidth(10);
                circleArray.add(circleInternal);
            }
        }
        Log.d("tag", "Додано круги в масив" + circleArray.size());
        for (int i = 1; i < circleArray.size(); i++) {
            for (int k = 0; k < circleArray.size() - i; k++) {
                CircleOptions curentCircle = circleArray.get(k);
                if (((curentCircle.getCenter().latitude - circleArray.get(k + 1).getCenter().latitude < 25) & (curentCircle.getCenter().latitude - circleArray.get(k + 1).getCenter().latitude > -25)) & ((curentCircle.getCenter().longitude - circleArray.get(k + 1).getCenter().longitude < 25) & (curentCircle.getCenter().longitude - circleArray.get(k + 1).getCenter().longitude > -25))) {
                    CircleOptions circle = new CircleOptions().center(curentCircle.getCenter()).fillColor(curentCircle.getFillColor()).radius(curentCircle.getRadius() + 0.3 * circleArray.get(k + 1).getRadius()).strokeColor(Color.TRANSPARENT);
                    circleArray.add(circle);
                    circleArray.remove(k);
                    circleArray.remove(k + 1);
                    Log.d("tag", "Знайдено подібні круги");
                }
            }
        }
        Log.d("tag", "sorted" + circleArray.size());
        for (CircleOptions circle : circleArray) {
            try {
                map.addCircle(circle);
            } catch (IllegalArgumentException e) {

            }
        }
    }
}
