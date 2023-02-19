package com.sll.sllkapturedataset.kapture.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class KPointCloud {
    private long timestamp;

    //pcd : pcd_index, x, y, z, confidence
    //depth : x, y, z, confidence
    private ArrayList<float[]> pcdArrays = new ArrayList<>();

    public static KPointCloud bufferToObject(long timestamp, FloatBuffer point){
        if(point.limit() > 0) {
            KPointCloud arCorePCD = new KPointCloud();
            ArrayList<float[]> pcdCoords = new ArrayList<>();

            for (int i = 0; i < point.limit(); i+=4) {
                float p00 = point.get(i);
                float p01 = point.get(i + 1);
                float p02 = point.get(i + 2);
                float p03 = point.get(i + 3);
                if(p03 > 0.3f){
                    //todo: check confidence
                    pcdCoords.add(new float[]{p00, p01, p02, p03});
                }
            }

            arCorePCD.setTimestamp(timestamp);
            arCorePCD.setPcdArrays(pcdCoords);
            return arCorePCD;
        }
        return null;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<float[]> getPcdArrays() {
        return pcdArrays;
    }

    public void setPcdArrays(ArrayList<float[]> pcdArrays) {
        this.pcdArrays = pcdArrays;
    }


}
