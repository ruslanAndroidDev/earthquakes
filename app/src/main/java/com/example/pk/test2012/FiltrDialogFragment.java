package com.example.pk.test2012;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.example.pk.test2012.uttil.Constants;
import com.example.pk.test2012.uttil.DialogListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pk on 28.12.2016.
 */

public class FiltrDialogFragment extends android.app.DialogFragment implements View.OnClickListener {

    RadioGroup typeRG;
    RadioGroup filtrRG;
    CardView applyCard;
    DialogListener.FiltrDialogListener listener;
    public FiltrDialogFragment(DialogListener.FiltrDialogListener filtrdialogListener){
        listener = filtrdialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyCustomDialogStyle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.BOTTOM);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 300;
        params.y = 500;
        window.setAttributes(params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.filter_popup, null);
        typeRG = (RadioGroup) v.findViewById(R.id.radioType);
        filtrRG = (RadioGroup) v.findViewById(R.id.typeFiltr);
        applyCard = (CardView) v.findViewById(R.id.cardApplay);
        applyCard.setOnClickListener(this);
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        int type_id = sPref.getInt(Constants.SHAREDPREF_KEY_TYPE, Constants.DEFAULT_RADIO_BTN_CHECKED_TYPE);
        int filtrId = sPref.getInt(Constants.SHAREDPREF_KEY_FILTER, Constants.DEFAULT_RADIO_BTN_CHECKED_FILTER_ID);
        typeRG.check(type_id);
        filtrRG.check(filtrId);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MyCustomDialogStyle;
        return dialog;
    }

    @Override
    public void onClick(View v) {
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
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_week) {
                filtrId = R.id.radio_btn_filtr_week;
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_mounth) {
                filtrId = R.id.radio_btn_filtr_mounth;
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
            }
        } else if (typeRG.getCheckedRadioButtonId() == R.id.radio_btn_type_significant) {
            typeId = R.id.radio_btn_type_significant;
            if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_today) {
                filtrId = R.id.radio_btn_filtr_today;
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson";
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_week) {
                filtrId = R.id.radio_btn_filtr_week;
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson";
            } else if (filtrRG.getCheckedRadioButtonId() == R.id.radio_btn_filtr_mounth) {
                filtrId = R.id.radio_btn_filtr_mounth;
                url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
            }
        }
        ed.putInt(Constants.SHAREDPREF_KEY_TYPE, typeId);
        ed.putInt(Constants.SHAREDPREF_KEY_FILTER, filtrId);
        ed.putString(Constants.SHAREDPREF_KEY_URL, url);
        ed.commit();
        dismiss();
        listener.OnFiltrChange(url);
    }
}
