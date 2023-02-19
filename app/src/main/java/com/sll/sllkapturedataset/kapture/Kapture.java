package com.sll.sllkapturedataset.kapture;

public enum Kapture {

    RECORD_CAMERA(0, "records_camera", "# timestamp, device_id, image_path")
    ,RECORD_DEPTH(1, "records_depth","# timestamp, device_id, depth_map_path") //
    ,RECORD_LIDAR(2, "records_lidar","# timestamp, device_id, point_cloud_path") // lidar data 는 after collecting
    ,RECORD_GNSS(3, "records_gnss","# timestamp, device_id, x, y, z, utc, dop")
    ,RIGS(3, "rigs","# rig_id, sensor_id, qw, qx, qy, qz, tx, ty, tz")
    ,SENSORS(4, "sensors","# sensor_id, name, sensor_type, [sensor_params]+")
    ,TRAJECTORIES(5, "trajectories","# timestamp, device_id, qw, qx, qy, qz, tx, ty, tz")

    /**
     *sll custom part
     */
    ,GPS_QUERY_MAP(6, "gps_query_map","# timestamp, latitude, longitude")
    ,SESSION(7, "session", "# timestamp, latitude, longitude, heading")
    ;

    private int idx;
    private String title;
    private String header;

    Kapture(int idx, String title, String header ){
        this.idx = idx;
        this.title = title;
        this.header = header;
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

}
