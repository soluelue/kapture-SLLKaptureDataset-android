package com.sll.sllkapturedataset.tools;

import android.content.Context;

public abstract class KaptureManager {
    private Context mContext;
    private ManagerListener.OnResultListener listener;
    private String deviceID;

    public KaptureManager(Context mContext, ManagerListener.OnResultListener listener, String deviceID){
        this.mContext = mContext;
        this.listener = listener;
        this.deviceID = deviceID;
    }

    public Context getContext() {
        return mContext;
    }

    public ManagerListener.OnResultListener getListener() {
        return listener;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    abstract public void start();
    abstract public void stop();
}
