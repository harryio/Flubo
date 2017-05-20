package com.harryio.flubo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {
    public static void hideKeyboard(Context context, View view) {
        view.clearFocus();
        ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
