package com.sll.sllkapturedataset.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.sll.sllkapturedataset.kapture.model.KGnss;

public class GNSSManager implements LocationListener {

    private Context mContext;
    private ManagerListener.OnResultListener listener;
    private LocationManager manager = null;

    private String deviceName;

    public GNSSManager(Context mContext, String deviceName, ManagerListener.OnResultListener listener) throws PermissionException {
        this.listener = listener;
        this.mContext = mContext;
        this.deviceName = deviceName;

        initLocationManager();
    }

    private void initLocationManager() throws PermissionException {
        this.manager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            throw new PermissionException(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        this.manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public void stopLocationManager(){
        if(this.manager != null){
            this.manager.removeUpdates(this::onLocationChanged);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        long timestamp = System.currentTimeMillis();
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        double altitude = location.getAltitude();
        long utc = location.getTime();
        double dop = 0;

        listener.onResult(new KGnss(timestamp, this.deviceName, lon, lat, altitude, utc, dop));
    }

}
