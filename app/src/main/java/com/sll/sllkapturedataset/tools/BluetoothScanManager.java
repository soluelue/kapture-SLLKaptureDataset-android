package com.sll.sllkapturedataset.tools;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import androidx.annotation.NonNull;

import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.model.KBluetooth;

/**
 * @brief BluetoothScanManager
 * @author soluelue
 * @version 1.0
 * @since 2023.04.02
 * @see KBluetooth
 * */
public class BluetoothScanManager extends KaptureManager {

    private BluetoothManager bleManager;
    private BluetoothAdapter bleAdapter;
    private BluetoothLeScanner bleScanner;


    public BluetoothScanManager(@NonNull Context mContext, @NonNull ManagerListener.OnResultListener listener, @NonNull String deviceID) {
        super(mContext, listener, deviceID);
        bleManager = (BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = bleManager.getAdapter();
        bleScanner = bleAdapter.getBluetoothLeScanner();
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            String address = result.getDevice().getAddress();
            int rssi = result.getRssi();
            String name = result.getDevice().getName();

            getListener().onResult(Kapture.RECORD_BLE, new KBluetooth(getCurrentTimestamp()
                    , getDeviceID(), address, rssi, name));
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        bleScanner.startScan(leScanCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void stop() {
        bleScanner.stopScan(leScanCallback);
    }
}
