package com.example.note_tejinder_android.utilities;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class NoteUtilities {
    public static void HideSoftKeyboard(Context mContext) {
        try {
            if (mContext != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                if (((Activity) mContext).getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
