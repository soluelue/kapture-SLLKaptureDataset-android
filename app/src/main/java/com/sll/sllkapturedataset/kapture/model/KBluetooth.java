package com.sll.sllkapturedataset.kapture.model;

import com.sll.sllkapturedataset.kapture.io.KIOManager;

/**
 * @brief timestamp, device_id, address, RSSI, name
 * @author soluelue
 * @version 1.0
 * @since 2023.04.02
 * */
public class KBluetooth extends KObject{
    private String address;
    private int rssi;
    private String name;

    public KBluetooth(long timestamp, String deviceID, String address, int rssi, String name) {
        super(timestamp, deviceID);
        this.address = address;
        this.rssi = rssi;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getTimestamp()); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.getDeviceID()); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.address); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.rssi); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.name);
        return stringBuilder.toString();
    }
}
