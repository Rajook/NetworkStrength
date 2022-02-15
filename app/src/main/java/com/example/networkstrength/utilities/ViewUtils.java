package com.example.networkstrength.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.example.networkstrength.MainActivity;
import com.example.networkstrength.MainActivityAlias;
import com.example.networkstrength.R;

public class ViewUtils {

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper, int theme) {
        if (theme==0){
            contextThemeWrapper.setTheme(R.style.AppTheme);
        }else {
            contextThemeWrapper.setTheme(R.style.AppThemeDarkActionBar);
        }
    }

    public static void changeAppIcon(MainActivity activity) {
        activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, MainActivityAlias.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, MainActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);

    }

    public static void returnOld(MainActivity activity) {
        activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, MainActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, MainActivityAlias.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
    }
}

