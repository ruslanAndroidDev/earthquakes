package com.example.pk.test2012.uttil;

import com.example.pk.test2012.R;

/**
 * Created by pk on 22.12.2016.
 */
public class Constants {
    public static final String DEFAULT_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    public static final String ALL_DAILY_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    public static final String ALL_WEEK_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    public static final String ALL_MONTH_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
    public static final String SIGNIFICANT_DAILY_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson";
    public static final String SIGNIFICANT_WEEK_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson";
    public static final String SIGNIFICANT_MONTH_URL_REQUEST = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    public static final int DEFAULT_RADIO_BTN_CHECKED_TYPE = R.id.radio_btn_type_significant;
    public static final int DEFAULT_RADIO_BTN_CHECKED_FILTER_ID = R.id.radio_btn_filtr_mounth;
    public static final int SIZE_MIN = 1;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_MAX = 3;
    public static final String SHAREDPREF_KEY_URL = "sp_krey_url";
    public static final String SHAREDPREF_KEY_TYPE = "tyype";
    public static final String SHAREDPREF_KEY_FILTER = "filter";
    public static final String SHAREDPREF_KEY_SORT = "sort";

    public static final int SORT_FLAG_POWERFUL_FIRST = 4;
    public static final int SORT_FLAG_WEAK_FIRST = 5;
    public static final int Sort_FLAG_DATE = 6;
    public static final int DEFAULT_SORT_FLAG = Sort_FLAG_DATE;

}
