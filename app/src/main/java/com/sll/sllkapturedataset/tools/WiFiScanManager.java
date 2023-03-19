package com.sll.sllkapturedataset.tools;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.core.app.ActivityCompat;

import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.model.KWiFi;

import java.util.ArrayList;
import java.util.List;

/** timestamp, device_id, BSSID, frequency, RSSI, SSID, scan_time_start, scan_time_end */
public class WiFiScanManager extends KaptureManager {

    private long startScanTime = 0L;

    private WifiManager wifiManager = null;
    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                try {
                    scanResult();
                }catch (PermissionException e){

                }

            }
        }
    };

    public WiFiScanManager(Context mContext, ManagerListener.OnResultListener listener, String deviceID) {
        super(mContext, listener, deviceID);
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void start() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getContext().registerReceiver(wifiScanReceiver, intentFilter);

        // todo: make TimeTask
        startScanTime = System.currentTimeMillis();
        wifiManager.startScan();
    }

    private void scanResult() throws PermissionException {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new PermissionException(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        }

        long endScanTime = System.currentTimeMillis();
        List<ScanResult> results = wifiManager.getScanResults();
        ArrayList<KWiFi> wifiList = new ArrayList<>();
        for (ScanResult scanResult : results){
            long timestamp = System.currentTimeMillis();
            String bssid = scanResult.BSSID;
            String ssid = scanResult.BSSID;
            int frequency = scanResult.frequency;
            int rssi = scanResult.level;
            KWiFi kWiFi = new KWiFi(timestamp, getDeviceID(),bssid, frequency, rssi, ssid, startScanTime, endScanTime);
            wifiList.add(kWiFi);
        }

        getListener().onResult(Kapture.RECORD_WIFI, wifiList);
    }

    @Override
    public void stop() {

    }
}
