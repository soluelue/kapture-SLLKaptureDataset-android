package com.sll.estimation.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Permissions {

    //permission
    private static final int PERMISSION_REQUEST_CODE = 700;

    private static String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};

    public interface OnPermissionListener{
        void onPermissionListener(boolean isAllSuccess);
    }

    public static void checkPermission(Context mContext, Activity mActivity, OnPermissionListener listener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //읽기 쓰기 권한 검사
            if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.HIGH_SAMPLING_RATE_SENSORS) != PackageManager.PERMISSION_GRANTED
            ) {
                //파일읽기 권한이 없을경우
                if (!mActivity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || !mActivity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                        || !mActivity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                        || !mActivity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                        || !mActivity.shouldShowRequestPermissionRationale(Manifest.permission.HIGH_SAMPLING_RATE_SENSORS)
                ) {
                    // 퍼미션 요청(시스템 팝업 생성)
                    // os 12 부터 블루투스 권한을 새로 받는다 .
                    ActivityCompat.requestPermissions(mActivity, permissions, PERMISSION_REQUEST_CODE);

                    // os 11 이상 부터 파일 작성 권한 받기
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if(!Environment.isExternalStorageManager()) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s", mContext.getApplicationContext().getPackageName())));
                            mActivity.startActivity(intent);
                        }
                    }

                } else {
                    // 권한요청 불가(항상 거절), 직접 유저가 설정창에서 권한을 허가해야하는 경우

                    listener.onPermissionListener(false);
                }
            } else {
                // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
                listener.onPermissionListener(true);
            }
        } else {
            listener.onPermissionListener(true);
        }
    }
}
