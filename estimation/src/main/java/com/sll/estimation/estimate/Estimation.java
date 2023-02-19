package com.sll.estimation.estimate;

import com.google.ar.core.Pose;
import com.sll.estimation.mat.MatrixF4x4;
import com.sll.estimation.mat.Quaternion;
import com.sll.estimation.mat.Vector3f;
import com.sll.estimation.model.Route;
import com.sll.estimation.model.Trajectory;

import java.util.ArrayList;

public class Estimation {

    private ArrayList<Trajectory> trajectories;

    public Estimation(ArrayList<Trajectory> trajectories){
        this.trajectories = trajectories;
    }

    public Route estimation(){

        ArrayList<Pose> interpolatePoses = new ArrayList<>();
        ArrayList<Long> keyTimes = new ArrayList<>();
        ArrayList<float[]> keyRotations = new ArrayList<>();
        ArrayList<Quaternion> interpolateRotations = new ArrayList<>();

        linearInterpolation(interpolatePoses, keyTimes, keyRotations, interpolateRotations);


        float[] ip0 = interpolatePoses.get(0).getTranslation();
        Vector3f transVector3f0 = new Vector3f(ip0[0], ip0[1], ip0[2]);
        Quaternion quaternion0 = interpolateRotations.get(0);

        ArrayList<Vector3f> relativeTranslations = new ArrayList<>();
        ArrayList<Quaternion> relativeQuaternions = new ArrayList<>();

        relativeRotationTranslation(interpolatePoses, interpolateRotations
                , relativeTranslations, relativeQuaternions);

        Route route = worldCoordinateFromRotationTranslation(keyTimes, transVector3f0, quaternion0
                , relativeTranslations, relativeQuaternions);

        return route;
    }

    private void linearInterpolation(ArrayList<Pose> interpolatePoses,
                                     ArrayList<Long> keyTimes,
                                     ArrayList<float[]> keyRotations,
                                     ArrayList<Quaternion> interpolateRotations){

        //initialize//
        Trajectory tj0 = trajectories.get(0);
        Pose pose0 = new Pose(tj0.getTranslation(), tj0.getRotationQuaternion());

        interpolatePoses.add(pose0);
        keyTimes.add(tj0.getTimestamp());
        interpolateRotations.add(new Quaternion(tj0.getRotationQuaternion()));
        keyRotations.add(tj0.getRotationQuaternion());


        /////////interpolation/////////////
        final float blendingFactor = 0.5f;

        for(int i = 1 ; i < trajectories.size(); i ++){
            Trajectory toTj = trajectories.get(i); // to

            Pose toPose = new Pose(toTj.getTranslation(), toTj.getRotationQuaternion());
            Pose nPose = Pose.makeInterpolated(interpolatePoses.get(i - 1), toPose, blendingFactor);

            interpolatePoses.add(nPose); // adding interpolation pose

            keyTimes.add(toTj.getTimestamp());
            keyRotations.add(nPose.getRotationQuaternion());
            interpolateRotations.add(new Quaternion(nPose.getRotationQuaternion())); // adding interpolation rotation
        }


    }

    private void relativeRotationTranslation(ArrayList<Pose> interpPoseArr, ArrayList<Quaternion> interpRotArr,
                                             ArrayList<Vector3f> relativeTransArr, ArrayList<Quaternion> relativeQuatArr){

        for(int i = 0 ; i < interpPoseArr.size() - 1; i ++){
            float[] ct = interpPoseArr.get(i).getTranslation();
            float[] nt = interpPoseArr.get(i + 1).getTranslation();

            Vector3f ctV = new Vector3f(ct[0], ct[1], ct[2]);
            Quaternion cq = new Quaternion(interpRotArr.get(i));

            Vector3f ntV = new Vector3f(nt[0], nt[1], nt[2]);
            Quaternion nq = new Quaternion(interpRotArr.get(i + 1));

            //next
            //(t_b-t_a)
            ntV.subtract(ctV); // subtract

            MatrixF4x4 tempCQ = cq.convertQuatToMatrix3X3();
            tempCQ.transpose();
            tempCQ.multiplyVector3fByMatrix(ntV);
            relativeTransArr.add(ntV);

            //relate quaternion
            cq = new Quaternion(interpRotArr.get(i));
            cq.conjugate();
            cq.multiplyByQuat(nq);
            forceQuaternionUniqueness(cq);
            relativeQuatArr.add(cq);

        }
    }

    private Route worldCoordinateFromRotationTranslation(ArrayList<Long> keyTimes, Vector3f trans, Quaternion quat,
                                                            ArrayList<Vector3f> transArr, ArrayList<Quaternion> quatArr){
        Route route = new Route();
        route.addCoordinate(keyTimes.get(0), trans.getX(), trans.getY(), trans.getZ());

        for(int i = 0; i < transArr.size(); i ++){
            Vector3f deltaT = transArr.get(i);
            Quaternion deltaQ = quatArr.get(i);

            MatrixF4x4 tempInitQuat = quat.convertQuatToMatrix3X3();
            tempInitQuat.multiplyVector3fByMatrix(deltaT);

            trans.add(deltaT);

            deltaQ.normalise();
            quat.multiplyByQuat(deltaQ);

            route.addCoordinate(keyTimes.get(i + 1), trans.getX(), trans.getY(), trans.getZ());
        }

        return route;
    }

    private void forceQuaternionUniqueness(Quaternion q){
        if(Math.abs(q.getW()) > 1e-05){
            if(Math.abs(q.getW()) < 0){
                q.multiplyByScalar(-1);
            }else{
                return;
            }
        }else if(Math.abs(q.getX()) > 1e-05){
            if(Math.abs(q.getX()) < 0){
                q.multiplyByScalar(-1);
            }else{
                return;
            }
        }else if(Math.abs(q.getY()) > 1e-05){
            if(Math.abs(q.getY()) < 0){
                q.multiplyByScalar(-1);
            }else{
                return;
            }
        }else{
            if(Math.abs(q.getZ()) < 0){
                q.multiplyByScalar(-1);
            }else{
                return;
            }
        }

    }


}
