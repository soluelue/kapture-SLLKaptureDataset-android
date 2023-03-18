package com.sll.sllkapturedataset.kapture.model;

import androidx.annotation.NonNull;

import com.sll.estimation.model.Trajectory;

public class KTrajectory extends Trajectory {

    private String deviceName = null;

    public KTrajectory(long timestamp) {
        super(timestamp);
    }

    public KTrajectory(long timestamp, float tx, float ty, float tz, float rqx, float rqy, float rqz, float rqw) {
        super(timestamp, tx, ty, tz, rqx, rqy, rqz, rqw);
    }

    public KTrajectory(long timestamp, String deviceName, float[] translation, float[] rotationQuaternion){
        super(timestamp, translation[0], translation[1], translation[2],
                rotationQuaternion[0], translation[1], translation[2], translation[3]);

        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTimestamp()); stringBuilder.append(',');
        stringBuilder.append(getDeviceName()); stringBuilder.append(',');
        stringBuilder.append(getRqw()); stringBuilder.append(',');
        stringBuilder.append(getRqx()); stringBuilder.append(',');
        stringBuilder.append(getRqy()); stringBuilder.append(',');
        stringBuilder.append(getRqz()); stringBuilder.append(',');
        stringBuilder.append(getTx()); stringBuilder.append(',');
        stringBuilder.append(getTy()); stringBuilder.append(',');
        stringBuilder.append(getTz());
        return stringBuilder.toString();
    }
}
