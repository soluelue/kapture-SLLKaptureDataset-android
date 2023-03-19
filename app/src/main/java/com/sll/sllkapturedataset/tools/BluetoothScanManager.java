package com.sll.sllkapturedataset.tools;

import android.content.Context;

public class BluetoothScanManager extends KaptureManager {
    //timestamp, device_id, address, RSSI, name


    public BluetoothScanManager(Context mContext, ManagerListener.OnResultListener listener, String deviceID) {
        super(mContext, listener, deviceID);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
