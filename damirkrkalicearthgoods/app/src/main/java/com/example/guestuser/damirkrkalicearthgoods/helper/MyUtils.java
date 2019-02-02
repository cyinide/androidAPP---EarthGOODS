package com.example.guestuser.damirkrkalicearthgoods.helper;


import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class MyUtils {

    public static <T> void popuniSpinner(Activity activity, List<T> podaci, int layoutId, int layoutItemId, Spinner el) {
        ArrayAdapter<T> dataAdapter = new ArrayAdapter<T>(activity, layoutId, podaci);
        dataAdapter.setDropDownViewResource(layoutItemId);
        el.setAdapter(dataAdapter);
    }

    public static void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getApplicationWindowToken(),
                    0);
        }
    }

}
