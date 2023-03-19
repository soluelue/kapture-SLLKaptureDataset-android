package com.sll.sllkapturedataset.kapture.model;

public class KGnss extends KObject {

    private float x;
    private float y;
    private float z;
    private long utc;
    private float dop;

    public KGnss(long timestamp, String deviceID, float x, float y, float z, long utc, float dop){
        super(timestamp, deviceID);
        this.x = x;
        this.y = y;
        this.z = z;
        this.utc = utc;
        this.dop = dop;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getTimestamp()); stringBuilder.append(",");
        stringBuilder.append(this.getDeviceID()); stringBuilder.append(",");
        stringBuilder.append(this.x); stringBuilder.append(",");
        stringBuilder.append(this.y); stringBuilder.append(",");
        stringBuilder.append(this.z); stringBuilder.append(",");
        stringBuilder.append(this.utc); stringBuilder.append(",");
        stringBuilder.append(this.dop);
        return stringBuilder.toString();
    }
}
