package com.sll.sllkapturedataset.kapture;

public enum Kapture {

    SENSORS(0, "sensors","# sensor_id, name, sensor_type, [sensor_params]+", false)
    ,RIGS(1, "rigs","# rig_id, sensor_id, qw, qx, qy, qz, tx, ty, tz", false)
    ,TRAJECTORIES(2, "trajectories","# timestamp, device_id, qw, qx, qy, qz, tx, ty, tz", false)
    ,RECORD_CAMERA(3, "records_camera", "# timestamp, device_id, image_path", false)
    ,RECORD_DEPTH(4, "records_depth","# timestamp, device_id, depth_map_path", true) //
    ,RECORD_GNSS(5, "records_gnss","# timestamp, device_id, x, y, z, utc, dop", true)
    ,RECORD_LIDAR(6, "records_lidar","# timestamp, device_id, point_cloud_path", true) // lidar data 는 after collecting
    ,RECORD_WIFI(7, "records_wifi","# timestamp, sensor id and wifi measurements", true)
    /**
     *sll custom part
     */
    , POSITION_QUERY_MAP(8, "gps_query_map","# timestamp, latitude, longitude", false)
    ,SESSION(9, "session", "# timestamp, latitude, longitude, heading", false)
    ;

    private int idx;
    private String title;
    private String header;
    private boolean optional;

    Kapture(int idx, String title, String header, boolean optional){
        this.idx = idx;
        this.title = title;
        this.header = header;
        this.optional = optional;
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
}
