package com.sll.sllkapturedataset.utils;

import android.util.Log;

public class SLLog {

    private static boolean DEBUG = true;
    private static final String TAG="-sll";

    public static void d(String tag, String msg){
        if(DEBUG) Log.d(tag + TAG, msg );
    }

    public static void e(String tag, String msg){
        if(DEBUG) Log.e(tag + TAG, msg );
    }

    public static void i(String tag, String msg){
        if(DEBUG) Log.i(tag + TAG, msg );
    }

    public static void w(String tag, String msg){
        if(DEBUG) Log.w(tag + TAG, msg );
    }
}
