package com.sll.sllkapturedataset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.CameraIntrinsics;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.exceptions.NotYetAvailableException;
import com.sll.arviewer.ar.ARFragment;
import com.sll.arviewer.ar.ARViewModel;
import com.sll.arviewer.ar.common.samplerender.LogType;
import com.sll.arviewer.ar.common.samplerender.OnUpdateListener;
import com.sll.estimation.utils.Permissions;
import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.io.GlobalPref;
import com.sll.sllkapturedataset.kapture.io.KIOManager;
import com.sll.sllkapturedataset.kapture.model.KGnss;
import com.sll.sllkapturedataset.kapture.model.KPointCloud;
import com.sll.sllkapturedataset.kapture.model.KTrajectory;
import com.sll.sllkapturedataset.tools.DepthManager;
import com.sll.sllkapturedataset.tools.GNSSManager;
import com.sll.sllkapturedataset.tools.ManagerListener;
import com.sll.sllkapturedataset.tools.PermissionException;
import com.sll.sllkapturedataset.utils.DateUtils;
import com.sll.sllkapturedataset.utils.DeviceUtils;
import com.sll.sllkapturedataset.utils.FileUtils;
import com.sll.sllkapturedataset.utils.ImageUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements OnUpdateListener, ManagerListener.OnResultListener {

    //UI Component
    private ImageButton btnMenu;
    private TextView txtLogView;
    private FloatingActionButton btnStart;

    //setting K//
    private final float PCD_CONFIDENCE = 0.3f; //pcd confidence over 30%

    private AtomicBoolean isStart = new AtomicBoolean(false);

    /// setting AR
    private ARViewModel viewModel = null;
    private ARFragment arFragment = null;

    private KIOManager kioManager = null;
    //Save the basic files(ex: rigs, sessions..) at the first run in case the app dies.
    private AtomicBoolean isRecordBaseInfo = new AtomicBoolean(false);

    //kapture dataset
    private GNSSManager gnssManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(ARViewModel.class);

        initPermissions();
        init();
    }

    private void initPermissions(){
        Permissions.checkPermission(getApplicationContext(), this, isAllSuccess -> {
            if(isAllSuccess){
                //todo: start code
                FileUtils.createRootPath();

            }else{
                Toast.makeText(getApplicationContext(), "Setting Permission yourself", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(){
        initARView();
        initUI();
    }

    private void initARView(){
        arFragment = ARFragment.newInstance(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_ar_view, arFragment).commit();

    }

    private void initUI(){
        btnStart = findViewById(R.id.btn_start);
        btnMenu = findViewById(R.id.btn_menu);
        txtLogView = findViewById(R.id.txt_logview);

        btnStart.setOnClickListener(v -> collectController());
        btnMenu.setOnClickListener(v-> {
            if(isStart.get()){
                Toast.makeText(getApplicationContext(), getString(R.string.alert_change_menu), Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(this, SettingsActivity.class));
            }
        });
    }

    private void initKaptureSetting(File recordDirPath){
        boolean[] useKaptureDataset = new boolean[Kapture.values().length];
        for(int i = 0; i < Kapture.values().length; i ++){
            Kapture kapture = Kapture.values()[i];
            useKaptureDataset[i] = true;
            if(kapture.isOptional()){
                if((kapture.getIdx() == Kapture.RECORD_DEPTH.getIdx())
                        && !GlobalPref.isUseDepth()){
                    useKaptureDataset[i] = false;
                }

                if((kapture.getIdx() == Kapture.RECORD_LIDAR.getIdx())
                        && !GlobalPref.isUseLidar()){
                    useKaptureDataset[i] = false;
                }

                if((kapture.getIdx() == Kapture.RECORD_GNSS.getIdx())
                        && !GlobalPref.isUseGNSS()){
                    useKaptureDataset[i] = false;
                }

                if((kapture.getIdx() == Kapture.RECORD_WIFI.getIdx())
                        && !GlobalPref.isUseWiFi()){
                    useKaptureDataset[i] = false;
                }
            }
        }

        kioManager = new KIOManager(recordDirPath, useKaptureDataset);
    }

    /**
     * @brief start kapture optional dataset manager(ex: gnss,,wifi,,)
     * */
    private void startManager(){
        if(GlobalPref.isUseGNSS()){
            try {
                gnssManager = new GNSSManager(getApplicationContext(), DeviceUtils.DEVICE_NAME, this);
            }catch (PermissionException e){

            }
        }
    }


    private void collectController(){

        if(isStart.get()){
            stopCollectDataset();
            isStart.set(false);
        } else {
            isStart.set(true);
            startCollectDataset();
        }

    }

    private void startCollectDataset(){
        //Kapture record
        File recordDirPath = new File(FileUtils.ROOT_DIR_NAME,
                DateUtils.getMM_dd_HH_mm_ss_SSS(System.currentTimeMillis()));
        FileUtils.createRecordPath(recordDirPath);

        initKaptureSetting(recordDirPath);

        startManager();
    }

    private void stopCollectDataset(){

        kioManager.stopRecords();
        isRecordBaseInfo.set(false);

        if(gnssManager != null) gnssManager.stopLocationManager();
    }

    @Override
    public void onResult(Object object) {

        if(kioManager == null) return;
        if(object instanceof KGnss){
             kioManager.recordGNSS(((KGnss)object).toString());
        }
    }

    @Override
    public void onUpdate(Frame frame) {
        if(viewModel.getSession() == null) return;
        if(!isStart.get() || kioManager == null) return;

        //using primary index key
        long timestamp = System.currentTimeMillis();

        Camera camera = frame.getCamera();
        Pose cameraPose = camera.getDisplayOrientedPose();

        //------------------------ Trajectory
        createTrajectories(timestamp, cameraPose);

        //------------------------ lidar & depth data
        createLidarDepthData(timestamp, frame, viewModel.getSession().createAnchor(cameraPose));

        //------------------------ Image & Base file
        Image image = null;
        try {
            //---------- Base File
            createBaseInformation(camera.getImageIntrinsics(), image.getWidth(), image.getHeight());
            //---------- Image File
            createImages(timestamp, frame.acquireCameraImage());
        } catch (NotYetAvailableException e) {
            e.printStackTrace();
        }finally {
            if(image != null) image.close();
        }

    }

    @Override
    public void onLog(LogType logType, String message) {
        if(logType == LogType.INFO){
            
        }
    }

    private void createBaseInformation(CameraIntrinsics cameraIntrinsics, int imageWidth, int imageHeight){
        if(isRecordBaseInfo.get()
                || cameraIntrinsics == null
                || DepthManager.DEPTH_WIDTH == -1 || DepthManager.DEPTH_HEIGHT == -1
                || imageWidth == 0 || imageHeight == 0 ) return;

        float[] focalLen = cameraIntrinsics.getFocalLength();
        float[] principalPoint = cameraIntrinsics.getPrincipalPoint();

        kioManager.recordRigs(DeviceUtils.RIG_NAME, DeviceUtils.DEVICE_NAME);
        kioManager.recordRigs(DeviceUtils.RIG_LIDAR_NAME, DeviceUtils.DEVICE_LIDAR_NAME);
        kioManager.recordRigs(DeviceUtils.RIG_DEPTH_NAME, DeviceUtils.DEVICE_DEPTH_NAME);

        // w, h, f, cx, cy
        String strCameraIntrinsics = focalLen[0] + "," + focalLen[1] + ","  + principalPoint[0] + "," + principalPoint[1];
        kioManager.recordSensors(DeviceUtils.DEVICE_NAME, DeviceUtils.CAMERA_TYPE
                , imageWidth, imageHeight, strCameraIntrinsics);
        kioManager.recordSensors(DeviceUtils.DEVICE_LIDAR_NAME, DeviceUtils.CAMERA_RIDAR_TYPE
                , DepthManager.DEPTH_WIDTH, DepthManager.DEPTH_HEIGHT, strCameraIntrinsics);
        kioManager.recordSensors(DeviceUtils.DEVICE_DEPTH_NAME, DeviceUtils.CAMERA_RIDAR_TYPE
                , DepthManager.DEPTH_WIDTH, DepthManager.DEPTH_HEIGHT, strCameraIntrinsics);

        //save the base information
        isRecordBaseInfo.set(true);
    }

    private void createTrajectories(long timestamp, Pose cameraPose){
        float[] translation = cameraPose.getTranslation();
        float[] rotationQuaternion = cameraPose.getRotationQuaternion();

        KTrajectory trajectory = new KTrajectory(timestamp, DeviceUtils.DEVICE_NAME, translation, rotationQuaternion);
        kioManager.recordTrajectories(trajectory.toString());
    }

    private void createImages(long timestamp, Image image){
        Bitmap bitmap = ImageUtils.yuv_420_888_toBitmap(image, getApplicationContext());

        String fileName = timestamp + KIOManager.EXE_IMAGE;
        ImageUtils.bitmapToSaveJpeg(bitmap, new File(kioManager.getRecordSubImagesPath(), fileName));
        kioManager.recordCamera(timestamp, DeviceUtils.DEVICE_NAME, fileName);
    }

    private void createLidarDepthData(long timestamp, Frame frame, Anchor anchor){
        //todo: memory leak checking
        Pair<FloatBuffer, ByteBuffer> pcdBuffer = DepthManager.create(frame, anchor);
        if(pcdBuffer == null) return;
        KPointCloud kPointCloud = KPointCloud.bufferToObject(timestamp, pcdBuffer.first, PCD_CONFIDENCE);

        //---------------- create lidar(point cloud) file
        String lidarFileName = timestamp + KIOManager.EXE_PCD;
        File lidarFile = new File(kioManager.getRecordSubLidarPath(), lidarFileName);
        KIOManager.recordLidarFile(lidarFile, kPointCloud.getPcdArrays().size(), kPointCloud.toString());
        kioManager.recordLidar(timestamp, DeviceUtils.DEVICE_LIDAR_NAME, lidarFileName);

        //---------------- create depth file
        String depthFileName = timestamp + KIOManager.EXE_DEPTH;
        File depthFile = new File(kioManager.getRecordSubDepthPath(), depthFileName);
        KIOManager.recordDepthFile(depthFile, pcdBuffer.second);
        kioManager.recordDepth(timestamp, DeviceUtils.DEVICE_DEPTH_NAME, depthFileName);

    }


}