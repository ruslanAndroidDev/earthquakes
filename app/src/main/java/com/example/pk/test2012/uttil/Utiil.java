package com.example.pk.test2012.uttil;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.pk.test2012.R;

/**
 * Created by pk on 24.12.2016.
 */
public class Utiil {
    public static int calculateCircleColor(Context context, double magnitude) {
        if (magnitude <= 1) {
            return ContextCompat.getColor(context, R.color.magnitude1);
        } else if (magnitude <= 2) {
            return ContextCompat.getColor(context, R.color.magnitude2);
        } else if (magnitude <= 3) {
            return ContextCompat.getColor(context, R.color.magnitude3);
        } else if (magnitude <= 4) {
            return ContextCompat.getColor(context, R.color.magnitude4);
        } else if (magnitude <= 5) {
            return ContextCompat.getColor(context, R.color.magnitude6);
        } else if (magnitude <= 6) {
            return ContextCompat.getColor(context, R.color.magnitude7);
        } else if (magnitude <= 7) {
            return ContextCompat.getColor(context, R.color.magnitude9);
        } else if (magnitude <= 12) {
            return ContextCompat.getColor(context, R.color.magnitude10);
        } else {
            return ContextCompat.getColor(context, R.color.colorAccent);
        }
    }

    public static int calculateCircleSize(Context context, double magnitude) {
        if (magnitude < 3) {
            return Constants.SIZE_MIN;
        } else if (magnitude < 6) {
            return Constants.SIZE_MEDIUM;
        } else if (magnitude < 12) {
            return Constants.SIZE_MAX;
        }
        return 1;
    }
}
