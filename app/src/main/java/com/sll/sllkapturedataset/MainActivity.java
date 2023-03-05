package com.sll.sllkapturedataset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.sll.arviewer.ar.ARFragment;
import com.sll.arviewer.ar.ARViewModel;
import com.sll.arviewer.ar.common.samplerender.LogType;
import com.sll.arviewer.ar.common.samplerender.OnUpdateListener;
import com.sll.arviewer.ar.common.samplerender.arcore.DepthData;
import com.sll.estimation.utils.Permissions;
import com.sll.sllkapturedataset.kapture.Kapture;
import com.sll.sllkapturedataset.kapture.io.GlobalPref;
import com.sll.sllkapturedataset.kapture.io.KIOManager;
import com.sll.sllkapturedataset.utils.DateUtils;
import com.sll.sllkapturedataset.utils.FileUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements OnUpdateListener {

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
    private CameraIntrinsics cameraIntrinsics = null;

    private KIOManager kioManager = null;


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

    }

    private void stopCollectDataset(){
        kioManager.stopRecords();
    }



    @Override
    public void onUpdate(Frame frame) {
        if(viewModel.getSession() == null) return;
        if(!isStart.get()) return;

        //using primary index key
        long timestamp = System.currentTimeMillis();

        Camera camera = frame.getCamera();
        cameraIntrinsics = camera.getImageIntrinsics();


        Pose cameraPose = camera.getDisplayOrientedPose();

        //depth data
        createDepthData(timestamp, frame, viewModel.getSession().createAnchor(cameraPose));

    }

    @Override
    public void onLog(LogType logType, String message) {

    }

    private void createDepthData(long timestamp, Frame frame, Anchor anchor){
        //todo: memory leak checking
        Pair<FloatBuffer, ByteBuffer> pcdBuffer = DepthData.create(frame, anchor);
        if(pcdBuffer == null) return;

    }
}