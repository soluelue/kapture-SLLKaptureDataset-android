package com.sll.sllkapturedataset.kapture.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * @author soluelue
 *
 * */
public class KPointCloud {
    private long timestamp;

    //pcd : pcd_index, x, y, z, confidence
    //depth : x, y, z, confidence
    private ArrayList<float[]> pcdArrays = new ArrayList<>();

    /**
     * @see KPointCloud
     * @brief change FloatBuffer to KPointcloud object
     * @param timestamp index
     * @param point point cloud data
     * @param standConfidence standard of confidence
     * */
    public static KPointCloud bufferToObject(long timestamp, FloatBuffer point, float standConfidence){
        if(point.limit() > 0) {
            KPointCloud kPointCloud = new KPointCloud();
            ArrayList<float[]> pcdCoords = new ArrayList<>();

            for (int i = 0; i < point.limit(); i+=4) {
                float x = point.get(i);
                float y = point.get(i + 1);
                float z = point.get(i + 2);
                float confidence = point.get(i + 3);

                if(confidence > standConfidence){
                    pcdCoords.add(new float[]{x, y, z, confidence});
                }
            }

            kPointCloud.setTimestamp(timestamp);
            kPointCloud.setPcdArrays(pcdCoords);
            return kPointCloud;
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
