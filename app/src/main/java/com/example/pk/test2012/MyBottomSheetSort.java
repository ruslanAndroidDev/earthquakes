package com.example.pk.test2012;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DialogListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pk on 04.01.2017.
 */

public class MyBottomSheetSort extends BottomSheetDialogFragment {
    RadioGroup sortRG;
    RadioButton powerful_first;
    RadioButton weak_first;
    RadioButton date;
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
        View v = inflater.inflate(R.layout.sort_popup, container, false);
        sortRG = (RadioGroup) v.findViewById(R.id.sortRG);
        powerful_first = (AppCompatRadioButton) v.findViewById(R.id.radio_btn_sort_powerful_first);
        weak_first = (AppCompatRadioButton) v.findViewById(R.id.radio_btn_sort_weak_first);
        date = (AppCompatRadioButton) v.findViewById(R.id.radio_btn_sort_date);
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        int sortflag = sPref.getInt(Constants.SHAREDPREF_KEY_SORT, Constants.DEFAULT_SORT_FLAG);
        int sortBtnId = 0;
        switch (sortflag) {
            case Constants.SORT_FLAG_POWERFUL_FIRST:
                sortBtnId = R.id.radio_btn_sort_powerful_first;
                break;
            case Constants.SORT_FLAG_WEAK_FIRST:
                sortBtnId = R.id.radio_btn_sort_weak_first;
                break;

            case Constants.Sort_FLAG_DATE:
                sortBtnId = R.id.radio_btn_sort_date;
                break;
        }
        sortRG.check(sortBtnId);
        return v;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        int sortRGCheckedId = sortRG.getCheckedRadioButtonId();
        int sortFlag = 0;
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        if (sortRGCheckedId == R.id.radio_btn_sort_date) {
            sortFlag = Constants.Sort_FLAG_DATE;
        } else if (sortRGCheckedId == R.id.radio_btn_sort_weak_first) {
            sortFlag = Constants.SORT_FLAG_WEAK_FIRST;
        } else if (sortRGCheckedId == R.id.radio_btn_sort_powerful_first) {
            sortFlag = Constants.SORT_FLAG_POWERFUL_FIRST;
        }
        ed.putInt(Constants.SHAREDPREF_KEY_SORT, sortFlag);
        ed.commit();
        listener.OnSortChange(sortFlag);
    }
}