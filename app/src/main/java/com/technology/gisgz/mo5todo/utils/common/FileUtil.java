package com.technology.gisgz.mo5todo.utils.common;

import android.os.Environment;
import android.util.Log;

import com.google.common.io.Files;
import com.technology.gisgz.mo5todo.utils.MyConfig;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jim.Lee on 2016/7/28.
 */
public class FileUtil {
    public static File createFile(String fileName) throws IOException {
        File dirApk = new File(Environment.getExternalStorageDirectory()+File.separator+"mo5todo"+File.separator+fileName);
//        if(dirApk.exists()){
//            Files.re
//        }
        Files.createParentDirs(dirApk);
        return dirApk;
    }
}
