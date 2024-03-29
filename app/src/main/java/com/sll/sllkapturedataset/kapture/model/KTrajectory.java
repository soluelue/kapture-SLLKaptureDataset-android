package com.sll.sllkapturedataset.kapture.model;

import androidx.annotation.NonNull;

import com.sll.estimation.model.Trajectory;

public class KTrajectory extends Trajectory {

    private String deviceID = null;

    public KTrajectory(long timestamp, String deviceID, float[] translation, float[] rotationQuaternion){
        super(timestamp, translation[0], translation[1], translation[2],
                rotationQuaternion[0], rotationQuaternion[1], rotationQuaternion[2], rotationQuaternion[3]);

        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTimestamp()); stringBuilder.append(',');
        stringBuilder.append(getDeviceID()); stringBuilder.append(',');
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
