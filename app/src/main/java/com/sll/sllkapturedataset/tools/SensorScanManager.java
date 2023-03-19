package com.sll.sllkapturedataset.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.model.KSensors;

public class SensorScanManager extends KaptureManager implements SensorEventListener{

    private SensorManager sensorManager;
    private int samplingRate = SensorManager.SENSOR_DELAY_FASTEST;

    private boolean isAccel = false;
    private boolean isMag = false;
    private boolean isGyro = false;

    /**
     * @see SensorManager
     * */
    public SensorScanManager(Context mContext, ManagerListener.OnResultListener listener, String deviceID){
        super(mContext, listener, deviceID);
    }

    @Override
    public void start() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if(isGyro) sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), this.samplingRate);
        if(isAccel) sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), this.samplingRate);
        if(isMag) sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), this.samplingRate);
    }

    @Override
    public void stop() {
        if(sensorManager != null) sensorManager.unregisterListener(this);
    }

    public void setSamplingRate(int samplingRate){
        this.samplingRate = samplingRate;
    }

    public void setAccel(boolean accel) {
        isAccel = accel;
    }

    public void setMag(boolean mag) {
        isMag = mag;
    }

    public void setGyro(boolean gyro) {
        isGyro = gyro;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (getListener() == null) return;
        long timestamp = System.currentTimeMillis();
        float[] r = event.values;
        KSensors kSensors = new KSensors(timestamp, getDeviceID(), r[0], r[1], r[2]);
        Kapture kapture = null;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: kapture = Kapture.RECORD_ACCEL; break;
            case Sensor.TYPE_GYROSCOPE: kapture = Kapture.RECORD_GYRO; break;
            case Sensor.TYPE_MAGNETIC_FIELD: kapture = Kapture.RECORD_MAG; break;
        }
        getListener().onResult(kapture, kSensors);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
