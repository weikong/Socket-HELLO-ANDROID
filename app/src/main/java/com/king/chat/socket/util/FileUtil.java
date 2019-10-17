package com.king.chat.socket.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maesinfo on 2019/10/17.
 */

public class FileUtil {

    private static class FileUtilHolder{
        private static final FileUtil INSTANCE = new FileUtil();
    }

    /**
     * 单一实例
     */
    public static final FileUtil getInstance(){
        return FileUtil.FileUtilHolder.INSTANCE;
    }

    public File saveFile(Bitmap bm, String fileName) throws IOException {//将Bitmap类型的图片转化成file类型，便于上传到服务器
        String path = SDCardUtil.getImgDir();
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path,fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;

    }
}
