package com.sll.sllkapturedataset.kapture.io;


import com.sll.estimation.utils.CSVWriter;
import com.sll.sllkapturedataset.kapture.Kapture;

import java.util.ArrayList;

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

    public KIOManager(String rootPath, ArrayList<Kapture> kaptures){

    }
}
