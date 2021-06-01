package com.example.mealstock.constants;

import android.content.Context;

import com.example.mealstock.BuildConfig;
import com.example.mealstock.R;


public class AppInfoConstants
{
    private static String appName;
    private static String appVersion;

    public AppInfoConstants(Context context){
        appName = context.getResources().getString(R.string.app_name);
        appVersion = BuildConfig.VERSION_NAME;
    }

    public static String getAppName() {
        return appName;
    }

    public static String getAppVersion() {
        return appVersion;
    }


}
