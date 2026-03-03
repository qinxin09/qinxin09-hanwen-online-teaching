package com.yws.onlinestudymanage.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private String ROOT;
    public static String getDirectoryPath(String userId,String path){
        Path filePath = Paths.get("src").toAbsolutePath();
        String subdirectory="\\hanwenresource\\userid"+userId;
        String userRootPath=filePath.toString()+subdirectory;
//        确保用户根目录存在
        ensurePath(userRootPath.toString());
//        判断是否存在路径
        String directoryPath  = userRootPath+path;
        File file = new File(directoryPath);
        if (file.exists()) {
            if(file.isDirectory()){
                System.out.println("this is folder and exists "+directoryPath);
                return directoryPath;
            }else{
                System.out.println("this is file and exists "+directoryPath);
                return directoryPath;
            }

        } else {
            System.out.println("path not exists "+directoryPath);
            return null;
        }

    }
    public static void ensurePath(String path){
        Path filePath = Paths.get(path).toAbsolutePath();
        if(!filePath.toFile().exists()){
            filePath.toFile().mkdirs();
        }
    }
}
