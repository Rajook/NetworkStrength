package com.example.networkstrength.utilities;

import android.view.ContextThemeWrapper;

import com.example.networkstrength.R;

public class ViewUtils {
    public static void applyTheme(ContextThemeWrapper contextThemeWrapper, int theme) {
        if (theme==0){
            contextThemeWrapper.setTheme(R.style.AppTheme);
        }else {
            contextThemeWrapper.setTheme(R.style.AppThemeDarkActionBar);
        }
    }
}
