package com.sll.sllkapturedataset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

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
import com.sll.sllkapturedataset.kapture.io.KIOManager;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnUpdateListener {

    //setting K//
    private final float PCD_CONFIDENCE = 0.3f; //pcd confidence over 30%


    /// setting AR
    private ARViewModel viewModel = null;
    private ARFragment arFragment = null;
    private CameraIntrinsics cameraIntrinsics = null;

    private KIOManager kioManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissions();
    }

    private void initPermissions(){
        Permissions.checkPermission(getApplicationContext(), this, new Permissions.OnPermissionListener() {
            @Override
            public void onPermissionListener(boolean isAllSuccess) {
                if(isAllSuccess){
                    //todo: start code
                    init();
                }else{
                    Toast.makeText(getApplicationContext(), "Setting Permission yourself", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(){
        initKaptureSetting();
        initARView();
    }

    private void initARView(){
        arFragment = ARFragment.newInstance(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_ar_view, arFragment).commit();

        viewModel = new ViewModelProvider(this).get(ARViewModel.class);
    }

    private void initKaptureSetting(){
        //create using kapture file
        ArrayList<Kapture> useKaptureArr = new ArrayList<>();
        useKaptureArr.add(Kapture.RECORD_CAMERA);
        useKaptureArr.add(Kapture.RECORD_DEPTH);
        useKaptureArr.add(Kapture.RECORD_LIDAR);
        useKaptureArr.add(Kapture.RIGS);
        useKaptureArr.add(Kapture.SENSORS);
        useKaptureArr.add(Kapture.TRAJECTORIES);
        useKaptureArr.add(Kapture.GPS_QUERY_MAP);
        useKaptureArr.add(Kapture.SESSION);

        kioManager = new KIOManager("", useKaptureArr);
    }


    @Override
    public void onUpdate(Frame frame) {
        if(viewModel.getSession() == null) return;

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