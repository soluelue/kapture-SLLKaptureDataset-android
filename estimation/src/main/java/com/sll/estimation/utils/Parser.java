package com.sll.estimation.utils;

import android.content.Context;
import android.util.Log;

import com.sll.estimation.model.Trajectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    private static final String TAG = Parser.class.getSimpleName();

    public static final String UTF8 = "UTF-8";

    public static ArrayList<ArrayList<String>> parser(Context mContext, String filePath, int startIdx , String regex){
        return parseStringArray(startIdx, regex, parseFile(mContext, filePath));
    }

    private static ArrayList<String> parseFile(Context mContext, String filePath){
        ArrayList<String> result = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(filePath), UTF8));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                result.add(mLine);
            }
        } catch (IOException e) {
            Log.d(TAG, e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }
            }
        }
        return result;
    }

    private static ArrayList<ArrayList<String>> parseStringArray(int startIdx , String regex, ArrayList<String> arrayList){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for(int i = startIdx; i < arrayList.size(); i ++){
            String[] splitArr = arrayList.get(i).trim().split(regex);
            result.add(new ArrayList<>(Arrays.asList(splitArr)));
        }
        return result;
    }

    public static ArrayList<Trajectory> makeTrajectories(ArrayList<ArrayList<String>> arrayList){
        ArrayList<Trajectory> result = new ArrayList<>();

        for(ArrayList<String> arr : arrayList){
            long timestamp = Long.parseLong(arr.get(0));
            float qw = Float.parseFloat(arr.get(1));
            float qx = Float.parseFloat(arr.get(2));
            float qy = Float.parseFloat(arr.get(3));
            float qz = Float.parseFloat(arr.get(4));
            float tx = Float.parseFloat(arr.get(5));
            float ty = Float.parseFloat(arr.get(6));
            float tz = Float.parseFloat(arr.get(7));

            result.add(new Trajectory(timestamp, tx, ty, tz, qx, qy, qz, qw));
        }
        return result;
    }

}
