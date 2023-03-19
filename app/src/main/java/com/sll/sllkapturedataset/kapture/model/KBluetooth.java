package com.sll.sllkapturedataset.kapture.model;

public class KBluetooth extends KObject{
    //timestamp, device_id, address, RSSI, name
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
        stringBuilder.append(this.getTimestamp()); stringBuilder.append(",");
        stringBuilder.append(this.getDeviceID()); stringBuilder.append(",");
        stringBuilder.append(this.address); stringBuilder.append(",");
        stringBuilder.append(this.rssi); stringBuilder.append(",");
        stringBuilder.append(this.name);
        return stringBuilder.toString();
    }
}
