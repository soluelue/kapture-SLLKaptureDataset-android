package com.sll.sllkapturedataset.kapture.io;


import androidx.annotation.NonNull;

import com.sll.estimation.utils.CSVWriter;
import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.utils.DeviceUtils;
import com.sll.sllkapturedataset.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * sensors/                         # Sensor data root path
 * │  ├─ sensors.txt                   # list of all sensors with their specifications (required)
 * │  ├─ rigs.txt                      # rigid geometric relationship between sensors
 * │  ├─ trajectories.txt              # extrinsics (timestamp, sensor, pose)
 * │  ├─ records_camera.txt            # recording of 'camera' (timestamp, sensor id and path to image)
 * │  ├─ records_depth.txt             # recording of 'depth' (timestamp, sensor id and path to depth map)
 * │  ├─ records_gnss.txt              # recording of 'GNSS' (timestamp, sensor id, location and ...)
 * │  ├─ records_lidar.txt             # recording of 'lidar' (timestamp, sensor id and path to lidar data)
 * │  ├─ records_wifi.txt              # recording of 'wifi' (timestamp, sensor id and wifi measurements)
 * │  ├─ records_SENSOR_TYPE.txt       # SENSOR_TYPE is replaced with custom type. (eg. 'magnetic', 'pressure' ...)
 * │  └─ records_data/                 # image and lidar data root path
 * │     ├─ mapping/cam_01/00001.jpg   # example of image path used in records_camera.txt
 * │     ├─ mapping/cam_01/00001.depth # example of depth map used in records_depth.txt
 * │     ├─ mapping/cam_01/00002.jpg
 * │     ├─ mapping/cam_01/00002.depth
 * │     ├─ mapping/lidar_01/0001.pcd  # example of lidar data path used in records_lidar.txt
 * │     ├─ query/query001.jpg         # example of image path used in records_camera.txt
 * │     └─ ...
 * */

public class KIOManager {

    private final String FILE_EXE = ".txt";
    private final String KAPTURE_HEADER = "# kapture format: 1.1";

    private final String SUBDIR_DEPTH = "depth";
    private final String SUBDIR_IMAGES = "images";
    private final String SUBDIR_LIDAR = "pcd";

    public final static String EXE_DEPTH = ".depth";
    public final static String EXE_PCD = ".pcd";
    public final static String EXE_IMAGE = ".jpg";

    private boolean[] useKaptures;

    private File recordPath;

    private File recordSubDepthPath = null;
    private File recordSubImagesPath = null;
    private File recordSubLidarPath = null;

    private CSVWriter[] kaptureWriters = null;

    public KIOManager(File recordPath, boolean[] useKaptures){
        this.recordPath = recordPath;
        this.useKaptures = useKaptures;

        createSubDirectory();
        createFiles();
    }

    private void createSubDirectory(){

        this.recordSubImagesPath = new File(this.recordPath, SUBDIR_IMAGES);
        FileUtils.createRecordPath(this.recordSubImagesPath);

        // optional folder
        if(this.useKaptures[Kapture.RECORD_DEPTH.getIdx()]){
            this.recordSubDepthPath = new File(this.recordPath, SUBDIR_DEPTH);
            FileUtils.createRecordPath(this.recordSubDepthPath);
        }

        // optional folder
        if(this.useKaptures[Kapture.RECORD_LIDAR.getIdx()]){
            this.recordSubLidarPath = new File(this.recordPath, SUBDIR_LIDAR);
            FileUtils.createRecordPath(this.recordSubLidarPath);
        }
    }

    private void createFiles(){
        kaptureWriters = new CSVWriter[Kapture.values().length];
        for(int i = 0; i < Kapture.values().length; i ++){
            if(useKaptures[i]){
                Kapture k = Kapture.values()[i];
                String filePath = this.recordPath.getAbsolutePath()
                        + File.separator
                        + k.getTitle()
                        + FILE_EXE;
                StringBuilder headerSb = new StringBuilder();
                headerSb.append(KAPTURE_HEADER);
                headerSb.append(System.lineSeparator());
                headerSb.append(k.getHeader());

                CSVWriter writer = new CSVWriter(filePath, headerSb.toString(), true);
                this.kaptureWriters[i] = writer;
            }else{
                this.kaptureWriters[i] = null;
            }
        }
    }

    public File getRecordSubDepthPath() {
        return recordSubDepthPath;
    }

    public File getRecordSubImagesPath() {
        return recordSubImagesPath;
    }

    public File getRecordSubLidarPath() {
        return recordSubLidarPath;
    }

    public void stopRecords(){
        for(int i = 0 ; i < Kapture.values().length; i ++){
            CSVWriter writer = this.kaptureWriters[i];
            if(writer != null){
                writer.close();
                //todo: checking
                this.kaptureWriters[i] = null;
            }
        }
    }

    private String recordFilePath(long timestamp, String deviceID, String fileName, String dirName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(timestamp); stringBuilder.append(",");
        stringBuilder.append(deviceID); stringBuilder.append(",");
        stringBuilder.append(dirName + File.separator + fileName);
        return stringBuilder.toString();
    }

    public void recordSensors(String deviceID, String cameraType, int width, int height, String cameraIntrinsics){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(deviceID); stringBuilder.append(",");
        stringBuilder.append(", ,camera,");
        stringBuilder.append(cameraType); stringBuilder.append(",");
        stringBuilder.append(width); stringBuilder.append(",");
        stringBuilder.append(height); stringBuilder.append(",");
        stringBuilder.append(cameraIntrinsics);
        recordSensors(stringBuilder.toString());
    }

    public void recordSensors(@NonNull String str){
        records(Kapture.SENSORS, str);
    }

    public void recordRigs(String rigName, String deviceID){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rigName); stringBuilder.append(",");
        stringBuilder.append(deviceID); stringBuilder.append(",  1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0");
        recordRigs(stringBuilder.toString());
    }

    public void recordRigs(@NonNull String str){
        records(Kapture.RIGS, str);
    }

    public void recordTrajectories(@NonNull String str){
        records(Kapture.TRAJECTORIES, str);
    }

    public void recordCamera(long timestamp, String deviceID, String fileName){
        recordCamera(recordFilePath(timestamp, deviceID, fileName, SUBDIR_IMAGES));
    }

    public void recordCamera(@NonNull String str){
        records(Kapture.RECORD_CAMERA, str);
    }

    public void recordDepth(long timestamp, String deviceID, String fileName){
        recordDepth(recordFilePath(timestamp, deviceID, fileName, SUBDIR_DEPTH));
    }

    public void recordDepth(@NonNull String str){
        records(Kapture.RECORD_DEPTH, str);
    }

    public void recordGNSS(@NonNull String str){
        records(Kapture.RECORD_GNSS, str);
    }

    public void recordAccel(@NonNull String str){
        records(Kapture.RECORD_ACCEL, str);
    }

    public void recordMag(@NonNull String str){
        records(Kapture.RECORD_MAG, str);
    }

    public void recordGyro(@NonNull String str){
        records(Kapture.RECORD_GYRO, str);
    }

    public void recordLidar(long timestamp, String deviceID, String fileName){
        recordLidar(recordFilePath(timestamp, deviceID, fileName, SUBDIR_LIDAR));
    }

    public void recordLidar(@NonNull String str){
        records(Kapture.RECORD_LIDAR, str);
    }

    public void recordWiFi(@NonNull String str){
        records(Kapture.RECORD_WIFI, str);
    }

    public void recordPositionQueryMap(@NonNull String str){
        records(Kapture.POSITION_QUERY_MAP, str);
    }

    public void recordSession(@NonNull String str){
        records(Kapture.SESSION, str);
    }


    private void records(Kapture kapture, @NonNull String str){
        if(this.useKaptures[kapture.getIdx()]){
            CSVWriter writer = this.kaptureWriters[kapture.getIdx()];
            if(writer != null){
                writer.write(str);
            }
        }
    }

    public static void recordDepthFile(File recordFile, ByteBuffer buf){
        FileChannel wChannel;
        if(!recordFile.exists()){
            try {
                wChannel = new FileOutputStream(recordFile, false).getChannel();
                wChannel.write(buf);
                wChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void recordLidarFile(File recordFile, int lidarCount, String info){

        FileWriter fileWriter = null;
        BufferedWriter bfWriter = null;

        if(!recordFile.exists()){
            try {
                recordFile.createNewFile();
                fileWriter = new FileWriter(recordFile,true);
                bfWriter = new BufferedWriter(fileWriter);
                //Write the header:
                bfWriter.write(".PCD v.7 - Point Cloud Data file format\n" +
                        "VERSION .7\n" +
                        "FIELDS x y z\n" +
                        "SIZE 4 4 4\n" +  //you only have 3 values xyz
                        "TYPE F F F\n" +
                        "COUNT 1 1 1\n" +
                        "WIDTH "+ lidarCount + "\n" +
                        "HEIGHT 1\n" +
                        "VIEWPOINT 0 0 0 1 0 0 0\n" +
                        "POINTS " + lidarCount + "\n" +
                        "DATA ascii \n");
                bfWriter.newLine();
                bfWriter.write(info);
                bfWriter.flush();
                fileWriter.close();
                bfWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
