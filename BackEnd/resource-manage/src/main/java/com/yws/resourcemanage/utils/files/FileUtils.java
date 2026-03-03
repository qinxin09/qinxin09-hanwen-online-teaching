package com.yws.resourcemanage.utils.files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileUtils {
    private String ROOT;
    public static String getUserRootDirectoryPath(String userId){
        Path filePath = Paths.get("src").toAbsolutePath();
        String subdirectory="\\hanwenresource\\userid"+userId;
        String userRootPath=filePath.toString()+subdirectory;
//        确保用户根目录存在
        ensurePath(userRootPath.toString());
        return userRootPath;
    }
    public static String getDirectoryPath(String userId,String path){
        Path filePath = Paths.get("src").toAbsolutePath();
        String subdirectory="\\hanwenresource\\userid"+userId;
        String userRootPath=filePath.toString()+subdirectory;
//        确保用户根目录存在
        ensurePath(userRootPath.toString());
//        判断是否存在路径
        String directoryPath  = userRootPath+path;
        System.out.println("filePath："+filePath);
        System.out.println("subdirectory："+subdirectory);
        System.out.println("userRootPath："+userRootPath);
        System.out.println("path："+path);

        System.out.println("directoryPath："+directoryPath);

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



    public static boolean createFolder(String path, String folderName) {
        String directoryPath = path + "/" + folderName;

        File directory = new File(directoryPath);

        if (directory.mkdirs()) {
            System.out.println("目录创建成功");
            return true;
        } else {
            System.out.println("目录创建失败");
            return false;
        }
    }

    public static boolean deleteDirectory(File folder) { //testok
        if (folder == null || !folder.exists()) {
            // 如果文件夹不存在，则直接返回
            return true;
        }

        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归调用自身
                    deleteDirectory(file);
                } else {
                    boolean result = file.delete();
                    if (result) {
                        System.out.println("删除文件：" + file.getAbsolutePath() + " 成功！");
                    } else {
                        System.err.println("删除文件：" + file.getAbsolutePath() + " 失败！");
                    }
                }
            }
        }

        boolean result = folder.delete();
        if (result) {
            System.out.println("删除目录：" + folder.getAbsolutePath() + " 成功！");
            return true;
        } else {
            System.err.println("删除目录：" + folder.getAbsolutePath() + " 失败！");
            return false;
        }
    }

    public static boolean rename(String directory, String newName) {
        System.out.println("重命名："+directory+"---------"+newName);
        return FileRenaming.rename(directory,newName);
    }

    public static void main(String[] args) {
//        getDirectoryPath("8","/");
//        createFolder("8", "test");
//        deleteDirectory(危险操作！！！));
    }
}
