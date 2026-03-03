package com.yws.resourcemanage.controller;

import com.yws.resourcemanage.utils.files.FileUtils;
import com.yws.resourcemanage.utils.Result;
import com.yws.resourcemanage.utils.files.DirectoryResponse;
import com.yws.resourcemanage.utils.files.FileDetail;
import com.yws.resourcemanage.utils.files.FileObj;
import com.yws.resourcemanage.utils.test.ChunkFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建文件夹
 * 删除文件
 * 重命名
 * 获取路径下的所有文件
 */
@RestController
@RequestMapping("/resource")
public class MyController {


    /**
     * 创建文件夹
     * @param dir 路径
     * @param userId 用户id
     * @param folderName 文件夹名
     * @return
     */
    @GetMapping("/create-folder")
    public Result createFolder(@RequestParam("dir") String dir,@RequestParam("userId") String userId ,@RequestParam("name") String folderName){
        //获取要创建文件夹的目录
        String directory=FileUtils.getDirectoryPath(userId+"",dir);
//        创建文件夹
        if(FileUtils.createFolder(directory,folderName)){
            return new Result().success("创建成功",null);
        }
        return new Result().error("文件已存在！",null);
    }

    /**
     * 删除文件
     * @param dir 路径
     * @param userId 用户id
     * @return
     */
    @GetMapping("/delete-path")
    public Result deletePath(@RequestParam("dir") String dir,@RequestParam("userid") int userId){
        //用户删除的路径是否存在
        String directory=FileUtils.getDirectoryPath(userId+"",dir);
        if(FileUtils.deleteDirectory(new File(directory))){
            System.out.println("删除路径成功"+directory);
            return new Result().success("删除成功",null);
        }
        return new Result().error("出错了",null);
    }

    /**
     * 重命名
     * @param dir 文件路径
     * @param userId 用户id
     * @param newName 新名称
     * @return
     */
    @GetMapping("/rename")
    public Result rename(@RequestParam("dir") String dir,@RequestParam("userid") int userId,@RequestParam("newname") String newName){
        //目标路径http://localhost:8300/resource/rename?userid=9&newname=hello&dir=/hello你好
        String directory=FileUtils.getDirectoryPath(userId+"",dir);
        if(FileUtils.rename(directory,newName)){
            System.out.println("命名成功"+directory);
            return new Result().success("命名成功",null);
        }
        return new Result().error("出错了",null);
    }
//    @GetMapping("/dir")
//    public ResponseEntity<DirectoryResponse> getDirectory(@RequestParam("dir") String dir,@RequestParam("userid") int userId) {
//        //判断用户访问的路径是否存在
//        String directory=FileUtils.getDirectoryPath(userId+"",dir);
//
//        if (directory == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//        List<FileDetail> folders = new ArrayList<>();
//        List<FileDetail> files = new ArrayList<>();
//        List<String> permission = new ArrayList<>();
//
//        File[] fileList = new File(directory).listFiles();
//        if (fileList != null) {
//            for (File file : fileList) {
//                try {
//                    String fileName = file.getName();
//
//                    FileDetail fileDetail = new FileDetail();
//                    fileDetail.setName(fileName);
//                    fileDetail.setSize(file.length());
//                    fileDetail.setMtime(file.lastModified());
//
//                    if (file.isDirectory()) {
//                        folders.add(fileDetail);
//                    } else {
//                        files.add(fileDetail);
//                    }
//                } catch (Exception e) {
//                    permission.add(file.getName());
//                }
//            }
//        }
//
//        DirectoryResponse response = new DirectoryResponse();
//        response.setCode(HttpStatus.OK.value());
//        response.setData(new FileObj(folders, files, permission));
//
//        return ResponseEntity.ok(response);
//    }

    /**
     * 获取路径下的所有文件
     * @param dir 路径
     * @param userId 用户id
     * @return
     */
    @GetMapping("/dir")
    public Result getDirectory(@RequestParam("dir") String dir,@RequestParam("userid") int userId) {
        //判断用户访问的路径是否存在
        String directory=FileUtils.getDirectoryPath(userId+"",dir);

        if (directory == null) {
            return new Result().error("获取路径失败！",null);
        }

        List<FileDetail> folders = new ArrayList<>();
        List<FileDetail> files = new ArrayList<>();
        List<String> permission = new ArrayList<>();

        File[] fileList = new File(directory).listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                try {
                    String fileName = file.getName();

                    FileDetail fileDetail = new FileDetail();
                    fileDetail.setName(fileName);
                    fileDetail.setSize(file.length());
                    fileDetail.setMtime(file.lastModified());

                    if (file.isDirectory()) {
                        folders.add(fileDetail);
                    } else {
                        files.add(fileDetail);
                    }
                } catch (Exception e) {
                    permission.add(file.getName());
                }
            }
        }
        return new Result().success("获取dir成功",new FileObj(folders,files,permission));
    }
}
