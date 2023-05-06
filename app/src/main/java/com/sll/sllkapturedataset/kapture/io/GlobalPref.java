package com.sll.sllkapturedataset.kapture.io;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author soluelue
 * @version 1.0
 * @since 2023.04.02
 * */
public class GlobalPref {

    private static final String PREFS_KEY = "SLLKaptureDataset";
    private SharedPreferences mPreferences = null;


    /**
     * setting kapture configuration
     * */
    public static final String CONFIG_SERVER_URL = "setting.server.url"; ///positioning server url
    public static final String CONFIG_SHOW_LOG = "setting.show.log"; ///positioning server url
    public static final String CONFIG_SHOW_DEPTH = "setting.show.depth"; ///positioning server url

    public static final String CONFIG_PCD_CONFIDENCE = "setting.kapture.pcd.confidence";

    public static final String CONFIG_USE_GNSS = "setting.kapture.gnss";
    public static final String CONFIG_USE_LIDAR = "setting.kapture.lidar";
    public static final String CONFIG_USE_DEPTH = "setting.kapture.depth";
    public static final String CONFIG_USE_WIFI = "setting.kapture.wifi";
    public static final String CONFIG_USE_BLE = "setting.kapture.bluetooth";
    public static final String CONFIG_USE_ACCEL = "setting.kapture.accelerometer";
    public static final String CONFIG_USE_GYRO = "setting.kapture.gyroscope";
    public static final String CONFIG_USE_MAG = "setting.kapture.magetic";

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


    public static boolean isShowLog(){
        return getPreferences().getBoolean(CONFIG_SHOW_LOG, false );
    }

    public static void setShowLog(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_SHOW_LOG, value);
        editor.apply();
    }

    public static boolean isShowDepth(){
        return getPreferences().getBoolean(CONFIG_SHOW_DEPTH, false );
    }

    public static void setShowDepth(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_SHOW_DEPTH, value);
        editor.apply();
    }



    public static boolean isUseDepth(){
        return getPreferences().getBoolean(CONFIG_USE_DEPTH, false );
    }

    public static void setUseDepth(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_DEPTH, value);
        editor.apply();
    }

    public static float getPcdConfidence(){
        return getPreferences().getFloat(CONFIG_PCD_CONFIDENCE, 0.3f);
    }

    public static void setPcdConfidence(float value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(CONFIG_PCD_CONFIDENCE, value);
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

    public static boolean isUseBLE(){
        return getPreferences().getBoolean(CONFIG_USE_BLE, false );
    }

    public static void setUseBLE(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_BLE, value);
        editor.apply();
    }


    public static boolean isUseAccel(){
        return getPreferences().getBoolean(CONFIG_USE_ACCEL, false );
    }

    public static void setUseAccel(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_ACCEL, value);
        editor.apply();
    }

    public static boolean isUseGyro(){
        return getPreferences().getBoolean(CONFIG_USE_GYRO, false );
    }

    public static void setUseGyro(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_GYRO, value);
        editor.apply();
    }

    public static boolean isUseMag(){
        return getPreferences().getBoolean(CONFIG_USE_MAG, false );
    }

    public static void setUseMag(boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(CONFIG_USE_MAG, value);
        editor.apply();
    }


}
