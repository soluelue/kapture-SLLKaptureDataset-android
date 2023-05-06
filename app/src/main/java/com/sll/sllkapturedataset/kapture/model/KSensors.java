package com.sll.sllkapturedataset.kapture.model;

import androidx.annotation.NonNull;

import com.sll.sllkapturedataset.kapture.io.KIOManager;

/**
 * @brief accelerometer = [timestamp, device_id, x_acc, y_acc, z_acc]
 * , gyroscope = [timestamp, device_id, x_speed, y_speed, z_speed]
 * , magnetic = [timestamp, device_id, x_strength, y_strength, z_strength]
 * @author soluelue
 * @version 1.0
 * @since 2023.04.02
 * @see KSensors
 * */
public class KSensors extends KObject {

    private float x;
    private float y;
    private float z;

    public KSensors(long timestamp, String deviceID, float x, float y, float z) {
        super(timestamp, deviceID);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getTimestamp()); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.getDeviceID()); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.x); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.y); stringBuilder.append(KIOManager.SEPARATOR);
        stringBuilder.append(this.z);
        return stringBuilder.toString();
    }
}
