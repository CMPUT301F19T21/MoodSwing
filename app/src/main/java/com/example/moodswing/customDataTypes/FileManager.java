package com.example.moodswing.customDataTypes;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileManager {
    String fileName;
    File file;
    public FileManager(String fileName){
        this.fileName = fileName+".jpg";
        String root = Environment.getExternalStorageDirectory().toString();
        File myFile = new File(root + "/MoodSwing");
        file = new File(myFile, this.fileName);
    }

    public void deleteImage(){
        file.delete();
    }

}
