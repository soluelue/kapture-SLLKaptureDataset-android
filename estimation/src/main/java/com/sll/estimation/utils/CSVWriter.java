package com.sll.estimation.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private final String TAG = this.getClass().getSimpleName();

    private File file = null;
    private FileWriter fileWriter = null;
    private BufferedWriter bfWriter = null;

    private String header = null;
    private boolean isExistFileDelete = true;

    public CSVWriter(String filePath){
        file = new File(filePath);
        createFile();
    }

    /**
     * @param filePath ex)root_path/filename.txt
     * @param header default: null
     * @param isExistFileDelete default : true
     * */
    public CSVWriter(String filePath, String header, boolean isExistFileDelete){
        this(filePath);

        this.header = header;
        this.isExistFileDelete = isExistFileDelete;

    }

    private void createFile(){

        if(isExistFileDelete) if(file.exists()) file.delete();

        if(!file.exists()) Log.d(TAG,"File doesn't exists");

        try {
            file.createNewFile();
            fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            bfWriter = new BufferedWriter(fileWriter);
            //header===============================================
            if(header != null) bfWriter.write(header);
            bfWriter.newLine();
            //header===============================================
            bfWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String str){
        if(bfWriter == null) return;
        try {
            bfWriter.write(str);
            bfWriter.newLine();
            bfWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            bfWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
