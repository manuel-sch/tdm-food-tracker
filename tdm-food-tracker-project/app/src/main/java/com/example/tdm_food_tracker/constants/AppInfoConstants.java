package com.example.tdm_food_tracker.constants;

import android.content.Context;

import com.example.tdm_food_tracker.BuildConfig;
import com.example.tdm_food_tracker.R;


public class AppInfoConstants
{
    private static String appName;
    private static Context appContext;
    private static String appVersion;

    public AppInfoConstants(Context context){
        appName = context.getResources().getString(R.string.app_name);
        appContext = context.getApplicationContext();
        appVersion = BuildConfig.VERSION_NAME;
    }

    public static String getAppName() {
        return appName;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static String getAppVersion() {
        return appVersion;
    }

}
