package com.example.moodswing.customDataTypes;

import android.os.Environment;

import java.io.File;

/**
 * a utility class for adding a photograph, unused
 */

public class FileManager {

    File file;
    public FileManager(){

        String root = Environment.getExternalStorageDirectory().toString();
        file = new File(root + "/MoodSwing");

    }

    /**
     * deletes an image with the corresponding filename
     * @param fileName the image string to be deleted
     */
    public void deleteImage(String fileName){
        File image = new File(file, fileName);
        image.delete();
    }
    // limit number of image
    public void deleteOldestFile(){
        File[] logFiles = file.listFiles();
        long oldestDate = Long.MAX_VALUE;
        File oldestFile = null;
        if( logFiles != null && logFiles.length >499){
            //delete oldest files after theres more than 500 log files
            for(File f: logFiles){
                if(f.lastModified() < oldestDate){
                    oldestDate = f.lastModified();
                    oldestFile = f;
                }
            }

            if(oldestFile != null){
                oldestFile.delete();
            }
        }
    }

}
