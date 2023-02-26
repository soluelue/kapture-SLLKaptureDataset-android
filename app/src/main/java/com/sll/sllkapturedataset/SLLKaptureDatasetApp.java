package com.sll.sllkapturedataset;

import android.app.Application;

import com.sll.sllkapturedataset.kapture.io.GlobalPref;

public class SLLKaptureDatasetApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalPref.initialize(getApplicationContext());
    }

}
