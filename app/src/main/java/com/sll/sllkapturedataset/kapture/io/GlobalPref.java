package com.sll.sllkapturedataset.kapture.io;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GlobalPref {

    private static final String PREFS_KEY = "SLLKaptureDataset";
    private SharedPreferences mPreferences = null;


    /**
     * setting kapture configuration
     * */
    public static final String CONFIG_USE_DEPTH = "setting.kapture.depth";
    public static final String CONFIG_USE_GNSS = "setting.kapture.gnss";
    public static final String CONFIG_USE_LIDAR = "setting.kapture.lidar";
    public static final String CONFIG_USE_WIFI = "setting.kapture.wifi";

    private static class LazyHolder {
        private static final GlobalPref INSTANCE = new GlobalPref();
    }

    private static GlobalPref getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    private static SharedPreferences getPreferences()
    {
        return LazyHolder.INSTANCE.mPreferences;
    }

    public static void initialize(Context context) {
        LazyHolder.INSTANCE.mPreferences = context.getSharedPreferences(PREFS_KEY, Activity.MODE_PRIVATE);
    }


    public static boolean isUseDepth(){
        return getPreferences().getBoolean(CONFIG_USE_DEPTH, false );
    }

    public static void setUseDepth(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_DEPTH, value);
        editor.apply();
    }


    public static boolean isUseGNSS(){
        return getPreferences().getBoolean(CONFIG_USE_GNSS, false );
    }

    public static void setUseGNSS(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_GNSS, value);
        editor.apply();
    }

    public static boolean isUseLidar(){
        return getPreferences().getBoolean(CONFIG_USE_LIDAR, false );
    }

    public static void setUseLidar(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_LIDAR, value);
        editor.apply();
    }


    public static boolean isUseWiFi(){
        return getPreferences().getBoolean(CONFIG_USE_WIFI, false );
    }

    public static void setUseWiFi(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_WIFI, value);
        editor.apply();
    }



}
