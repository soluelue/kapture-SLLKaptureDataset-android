package com.sll.sllkapturedataset.kapture.model;

public class KWiFi extends KObject {

    private String bssid;
    private int frequency;
    private int rssi;
    private String ssid;
    private long scanTimeStart = 0L;
    private long scanTimeEnd = 0L;

    public KWiFi(long timestamp, String deviceID, String bssid, int frequency, int rssi
            , String ssid, long scanTimeStart, long scanTimeEnd){
        super(timestamp, deviceID);
        this.bssid = bssid;
        this.frequency = frequency;
        this.rssi = rssi;
        this.ssid = ssid;
        this.scanTimeStart = scanTimeStart;
        this.scanTimeEnd = scanTimeEnd;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getTimestamp()); stringBuilder.append(",");
        stringBuilder.append(this.getDeviceID()); stringBuilder.append(",");
        stringBuilder.append(this.bssid); stringBuilder.append(",");
        stringBuilder.append(this.frequency); stringBuilder.append(",");
        stringBuilder.append(this.rssi); stringBuilder.append(",");
        stringBuilder.append(this.ssid); stringBuilder.append(",");
        stringBuilder.append(this.scanTimeStart); stringBuilder.append(",");
        stringBuilder.append(this.scanTimeEnd);
        return stringBuilder.toString();
    }
}
