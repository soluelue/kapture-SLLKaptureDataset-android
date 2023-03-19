package com.sll.sllkapturedataset.kapture.model;

public class KObject {
    private long timestamp;
    private String deviceID;

    public KObject(long timestamp, String deviceID){
        this.timestamp = timestamp;
        this.deviceID = deviceID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
