package com.sll.sllkapturedataset.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.model.KGnss;

public class GNSSManager extends KaptureManager implements LocationListener {
    private LocationManager manager = null;

    public GNSSManager(@NonNull Context mContext, @NonNull ManagerListener.OnResultListener listener, @NonNull String deviceID) throws PermissionException {
        super(mContext, listener, deviceID);
        initLocationManager();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        this.manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void stop() {
        if(this.manager != null){
            this.manager.removeUpdates(this::onLocationChanged);
        }
    }

    private void initLocationManager() throws PermissionException {
        this.manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            throw new PermissionException(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION});
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float lat = (float) location.getLatitude();
        float lon = (float) location.getLongitude();
        float altitude = (float) location.getAltitude();
        long utc = location.getTime();
        float dop = 0f;

        getListener().onResult(Kapture.RECORD_GNSS, new KGnss(getCurrentTimestamp(), getDeviceID(),
                lon, lat, altitude, utc, dop));
    }

}
