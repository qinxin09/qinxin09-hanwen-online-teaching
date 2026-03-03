package com.yws.resourcemanage.utils.files;

import java.io.File;

public class FileRenaming {
    public static boolean rename(String directory, String newName) {
        File file = new File(directory);

        // 判断文件或目录是否存在
        if (!file.exists()) {
            System.out.println("文件或目录不存在");
            return false;
        }

        // 判断当前文件是否为目录
        if (file.isDirectory()) {
            String newDirectoryPath = file.getParent() + "/" + newName;
            File newDirectory = new File(newDirectoryPath);

            if (file.renameTo(newDirectory)) {
                System.out.println("目录重命名成功");
                return true;
            } else {
                System.out.println("目录重命名失败");
                return false;
            }
        } else { // 当前文件为文件
            String newFilePath = file.getParent() + "/" + newName;
            File newFile = new File(newFilePath);

            if (file.renameTo(newFile)) {
                System.out.println("文件重命名成功");
                return true;
            } else {
                System.out.println("文件重命名失败");
                return false;
            }
        }
    }

    public static void main(String[] args) {
        String directory = "E:/test/newName/qx编程笔记.md";
        String newName = "qx编程笔记1234.md";

        rename(directory, newName);
    }
}