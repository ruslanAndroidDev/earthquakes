package com.example.pk.test2012.uttil;

/**
 * Created by pk on 29.12.2016.
 */

public interface DialogListener {
    interface FiltrDialogListener {
        void OnFiltrChange(String newRequestUrl);
    }

    interface SortDialogListener {
        void OnSortChange(int flag);
    }
}
