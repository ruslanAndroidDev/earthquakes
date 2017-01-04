package com.example.pk.test2012;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DialogListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pk on 04.01.2017.
 */

public class MyBottomSheetSort extends BottomSheetDialogFragment {
    RadioGroup typeRG;
    RadioGroup filtrRG;
    DialogListener.SortDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyBottomSheetSort(DialogListener.SortDialogListener sortdialogListener) {
        listener = sortdialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_popup, container, false);
        typeRG = (RadioGroup) v.findViewById(R.id.radioType);
        filtrRG = (RadioGroup) v.findViewById(R.id.typeFiltr);
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        int type_id = sPref.getInt(Constants.SHAREDPREF_KEY_TYPE, Constants.DEFAULT_RADIO_BTN_CHECKED_TYPE);
        int filtrId = sPref.getInt(Constants.SHAREDPREF_KEY_FILTER, Constants.DEFAULT_RADIO_BTN_CHECKED_FILTER_ID);
        typeRG.check(type_id);
        filtrRG.check(filtrId);
        return v;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        int typeRGCheckedRadioButtonId = typeRG.getCheckedRadioButtonId();
        int typeFiltrId = filtrRG.getCheckedRadioButtonId();
        Log.d("tag", typeRGCheckedRadioButtonId + " typeRg," + typeFiltrId);
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        int typeId = 0;
        int filtrId = 0;
        String url = "";
        if (typeRG.getCheckedRadioButtonId() == R.id.radio_btn_type_all) {
            typeId = R.id.radio_btn_type_all;
            if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_today) {
                filtrId = R.id.radio_btn_filtr_today;
                url = Constants.ALL_DAILY_URL_REQUEST;

            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_week) {
                filtrId = R.id.radio_btn_filtr_week;
                url = Constants.ALL_WEEK_URL_REQUEST;
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_mounth) {
                filtrId = R.id.radio_btn_filtr_mounth;
                url = Constants.ALL_MONTH_URL_REQUEST;
            }
        } else if (typeRG.getCheckedRadioButtonId() == R.id.radio_btn_type_significant) {
            typeId = R.id.radio_btn_type_significant;
            if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_today) {
                filtrId = R.id.radio_btn_filtr_today;
                url = Constants.SIGNIFICANT_DAILY_URL_REQUEST;
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_week) {
                filtrId = R.id.radio_btn_filtr_week;
                url = Constants.SIGNIFICANT_WEEK_URL_REQUEST;
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_mounth) {
                filtrId = R.id.radio_btn_filtr_mounth;
                url = Constants.SIGNIFICANT_MONTH_URL_REQUEST;
            }
        }
        ed.putInt(Constants.SHAREDPREF_KEY_TYPE, typeId);
        ed.putInt(Constants.SHAREDPREF_KEY_FILTER, filtrId);
        ed.putString(Constants.SHAREDPREF_KEY_URL, url);
        ed.commit();
        listener.OnSortChange(0);
    }
}