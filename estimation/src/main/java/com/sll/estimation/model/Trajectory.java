package com.sll.estimation.model;

public class Trajectory {
    private long timestamp;

    /**
     * translation x
    * */
    private float tx;

    /**
     * translation y
     * */
    private float ty;

    /**
     * translation z
     * */
    private float tz;

    /**
     * rotation quaternion x
     * */
    private float rqx;

    /**
     * rotation quaternion y
     * */
    private float rqy;

    /**
     * rotation quaternion z
     * */
    private float rqz;

    /**
     * rotation quaternion w
     * */
    private float rqw;

    public Trajectory(long timestamp){
        this.timestamp = timestamp;
    }

    public Trajectory(long timestamp, float tx, float ty, float tz, float rqx, float rqy, float rqz, float rqw){
        this(timestamp);

        this.tx = tx * -1;
        this.ty = ty;
        this.tz = tz * -1;

        this.rqx = rqx;
        this.rqy = rqy;
        this.rqz = rqz;
        this.rqw = rqw;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getTx() {
        return tx;
    }

    public void setTx(float tx) {
        this.tx = tx;
    }

    public float getTy() {
        return ty;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }

    public float getTz() {
        return tz;
    }

    public void setTz(float tz) {
        this.tz = tz;
    }

    public float getRqx() {
        return rqx;
    }

    public void setRqx(float rqx) {
        this.rqx = rqx;
    }

    public float getRqy() {
        return rqy;
    }

    public void setRqy(float rqy) {
        this.rqy = rqy;
    }

    public float getRqz() {
        return rqz;
    }

    public void setRqz(float rqz) {
        this.rqz = rqz;
    }

    public float getRqw() {
        return rqw;
    }

    public void setRqw(float rqw) {
        this.rqw = rqw;
    }

    /**
     * @return array order is [z, x, y]
     * */
    public float[] getTranslation(){
        return new float[]{tz, tx, ty};
    }

    /**
     * @return array order is [x, y, z, w]
     * */
    public float[] getRotationQuaternion(){
        return new float[]{rqx, rqy, rqz, rqw};
    }
}
