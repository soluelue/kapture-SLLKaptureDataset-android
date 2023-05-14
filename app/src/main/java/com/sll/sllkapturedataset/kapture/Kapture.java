package com.sll.sllkapturedataset.kapture;
// https://github.com/naver/kapture/blob/main/kapture_format.adoc
public enum Kapture {

    SENSORS(0, "sensors","# sensor_id, name, sensor_type, [sensor_params]+", false, "sensors")
    ,RIGS(1, "rigs","# rig_id, sensor_id, qw, qx, qy, qz, tx, ty, tz", false, "rigs")
    ,TRAJECTORIES(2, "trajectories","# timestamp, device_id, qw, qx, qy, qz, tx, ty, tz", false, "traj")
    ,RECORD_CAMERA(3, "records_camera", "# timestamp, device_id, image_path", false, "cam")
    ,RECORD_DEPTH(4, "records_depth","# timestamp, device_id, depth_map_path", true, "depth") //
    ,RECORD_GNSS(5, "records_gnss","# timestamp, device_id, x, y, z, utc, dop", true, "gnss")
    ,RECORD_LIDAR(6, "records_lidar","# timestamp, device_id, point_cloud_path", true, "lidar") // lidar data 는 after collecting
    ,RECORD_WIFI(7, "records_wifi","# timestamp, device_id, BSSID, frequency, RSSI, SSID, scan_time_start, scan_time_end", true, "ap")
    ,RECORD_BLE(8, "records_bluetooth","# timestamp, device_id, address, RSSI, name", true, "ble")
    ,RECORD_ACCEL(9, "records_accelerometer","# timestamp, device_id, x_acc, y_acc, z_acc", true, "acc")
    ,RECORD_GYRO(10, "records_gyroscope","# timestamp, device_id, x_speed, y_speed, z_speed", true, "gyro")
    ,RECORD_MAG(11, "records_magnetic","# timestamp, device_id, x_strength, y_strength, z_strength", true, "mag")

   /**
     *sll custom part
     */
    , POSITION_QUERY_MAP(12, "gps_query_map","# timestamp, latitude, longitude", false, "query")
    ,SESSION(13, "session", "# timestamp, latitude, longitude, heading", false,"session")
    ;

    private int idx;
    private String title;
    private String header;
    private boolean optional;
    private String shortName;

    Kapture(int idx, String title, String header, boolean optional, String shortName){
        this.idx = idx;
        this.title = title;
        this.header = header;
        this.optional = optional;
        this.shortName = shortName;
    }

    public int getIdx() {
        return idx;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getShortName() {
        return shortName;
    }
}
