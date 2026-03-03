package com.yws.resourcemanage.utils.test;

import com.yws.resourcemanage.utils.Result;
import com.yws.resourcemanage.utils.files.FileUtils;
import com.yws.resourcemanage.utils.files.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
public class ChunkFileUpload {
    // 设置文件存储目录
    private static final String UPLOAD_DIR = "F:/test/test/test3"; // 替换为实际的存储路径

    // 定义文件分片存储方法
    private void storeChunk(byte[] chunk, String filename, int chunkIndex) throws IOException {
        if(chunkIndex==0){
            FileUtils.ensurePath(UPLOAD_DIR);
        }
        String chunkPath = UPLOAD_DIR + File.separator + filename + "-" + chunkIndex;
        Files.write(Paths.get(chunkPath), chunk);
    }

    @PostMapping("/chunk-upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("filename") String filename,
                             @RequestParam("chunkIndex") int chunkIndex,
                             @RequestParam("totalChunks") int totalChunks) {
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
        try (FileOutputStream fos = new FileOutputStream(mergedFilePath, true)) {
            for (int i = 0; i < totalChunks; i++) {
                String chunkPath = UPLOAD_DIR + File.separator + filename + "-" + i;
                byte[] chunk = Files.readAllBytes(Paths.get(chunkPath));
                fos.write(chunk);
                Files.delete(Paths.get(chunkPath)); // 合并后删除分片块文件
            }
        }
    }
}
