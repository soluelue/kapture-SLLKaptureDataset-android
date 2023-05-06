package com.sll.sllkapturedataset.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String ROOT_DIR_NAME = SDCARD_PATH + File.separator+ "SLLKapture";

    public static boolean createRootPath(){
        return createDir(new File(FileUtils.ROOT_DIR_NAME), false);
    }

    public static boolean createRecordPath(File file){
        return createDir(file, true);
    }

    public static boolean createRecordPath(String rootDirPath, String recordDirName){
        return createDir(new File(rootDirPath, recordDirName), true);
    }

    private static boolean createDir(File file, boolean isDelete){
        boolean isResult = false;
        if(file.exists()){
            if(isDelete) file.delete();
        }
        isResult = file.mkdir();
        return isResult;
    }

}
