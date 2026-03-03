package com.yws.resourcemanage.controller;

import com.yws.resourcemanage.utils.Result;
import com.yws.resourcemanage.utils.files.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 分片上传文件
 */
@RestController
@RequestMapping("/resource")
public class UploadController {
    // 设置文件存储目录
    private static String UPLOAD_DIR = "D:/F/test/test/test3"; // 替换为实际的存储路径

    // 定义文件分片存储方法
    private void storeChunk(byte[] chunk, String filename, int chunkIndex) throws IOException {
        if(chunkIndex==0){
            FileUtils.ensurePath(UPLOAD_DIR);
        }
        String chunckTempPath = UPLOAD_DIR + File.separator + filename+"_folder" +File.separator;

        if(! new File(chunckTempPath).exists()){
            new File(chunckTempPath).mkdirs();
        }

        String chunkPath = chunckTempPath + filename + "-" + String.valueOf(chunkIndex==0?"0":chunkIndex);
        Files.write(Paths.get(chunkPath), chunk);
    }

    /**
     * 分片上传文件
     * @param file 文件
     * @param filename 文件名
     * @param chunkIndex 分片顺序索引
     * @param totalChunks 总分片数量
     * @param userId 用户id
     * @param path 文件路径
     * @return
     */
    @PostMapping("/chunk-upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("filename") String filename,
                             @RequestParam("chunkIndex") int chunkIndex,
                             @RequestParam("totalChunks") int totalChunks,
                             @RequestParam("userId") String userId,
                             @RequestParam("path") String path) {
        //判断用户访问的路径是否存在
        String directory=FileUtils.getDirectoryPath(userId+"",path);

        if (directory == null) {
            return new Result().error("路径错误",null);
        }

//        设置保存路径
        this.UPLOAD_DIR=directory;

        Map<String,Object> map=new HashMap<>();
        System.out.format("filename=%s chunkIndex=%d totalChunks=%d\n",filename,chunkIndex,totalChunks);
        try {
            // 读取分片块字节数据
            byte[] chunk = file.getBytes();

            // 存储分片块
            storeChunk(chunk, filename, chunkIndex);

            if (chunkIndex == totalChunks - 1) {
                // 最后一个分片块，进行文件合并的操作
                mergeChunks(filename, totalChunks);

                // 返回成功上传的信息
//                return ResponseEntity.ok().body(new ResponseMessage("文件上传成功"));
                map.put("state","complete");
                System.out.println("文件"+filename+"保存在："+this.UPLOAD_DIR);
                return new Result().success("文件上传成功",map);
            }

            // 返回成功接收分片块的信息
//            return ResponseEntity.ok().body(new ResponseMessage("分片块上传成功"));
            map.put("state","uploading");
            return new Result().success("分片块上传成功",null);
        } catch (IOException e) {
            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("上传失败"));
            map.put("state","failed");
            return new Result().error("上传失败",map);
        }
    }

    // 定义文件合并方法
    private void mergeChunks(String filename, int totalChunks) throws IOException {
        String mergedFilePath = UPLOAD_DIR + File.separator + filename;
        String chunckTempPath = UPLOAD_DIR + File.separator + filename+"_folder" +File.separator;//分片路径

        try (FileOutputStream fos = new FileOutputStream(mergedFilePath, true)) {
            for (int i = 0; i < totalChunks; i++) {
                String chunkPathFile = chunckTempPath + filename + "-" + i;
                byte[] chunk = Files.readAllBytes(Paths.get(chunkPathFile));
                fos.write(chunk);
                Files.delete(Paths.get(chunkPathFile)); // 合并后删除分片块文件
            }
            Files.delete(Paths.get(chunckTempPath));
        }
    }
}
