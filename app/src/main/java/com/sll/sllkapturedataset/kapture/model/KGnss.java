package com.sll.sllkapturedataset.kapture.model;

public class KGnss {

    private long timestamp;
    private String deviceName;
    private double x;
    private double y;
    private double z;
    private long utc;
    private double dop;

    public KGnss(long timestamp, String deviceName, double x, double y, double z, long utc, double dop){
        this.timestamp = timestamp;
        this.deviceName = deviceName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.utc = utc;
        this.dop = dop;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.timestamp); stringBuilder.append(",");
        stringBuilder.append(this.deviceName); stringBuilder.append(",");
        stringBuilder.append(this.x); stringBuilder.append(",");
        stringBuilder.append(this.y); stringBuilder.append(",");
        stringBuilder.append(this.z); stringBuilder.append(",");
        stringBuilder.append(this.utc); stringBuilder.append(",");
        stringBuilder.append(this.dop);
        return stringBuilder.toString();
    }
}
