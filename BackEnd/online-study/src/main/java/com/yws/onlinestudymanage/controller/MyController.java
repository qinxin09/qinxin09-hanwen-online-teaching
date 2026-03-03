package com.yws.onlinestudymanage.controller;

import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.service.myservice.MyService;
import com.yws.onlinestudymanage.utils.FileUtils;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 在线播放视频
 * 在线预览pdf
 * 获取指定课程信息
 * 获取教师信息
 */
@RestController
@RequestMapping("/online-study")
public class MyController {

    @Autowired
    MyService myService;
//    在线播放视频
    @GetMapping(value = "/video-play", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> playVideo(
            @RequestParam("teacherId") Long teacherId,
            @RequestParam("filePath") String filePath
    ) throws IOException {
        String filepath = FileUtils.getDirectoryPath(String.valueOf(teacherId),filePath);
        System.out.print("文件路径"+filepath);
        File video = new File(filepath);
        InputStream stream = new FileInputStream(video);
        Resource resource = new InputStreamResource(stream);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Ranges", "bytes"); // 添加Accept-Ranges头部字段
        byte[] videoBytes = Files.readAllBytes(Paths.get(filepath));
        headers.setContentLength(videoBytes.length); // 添加Content-Length头部字段
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * 在线预览pdf
     * @param teacherId
     * @param filePath
     * @return
     * @throws IOException
     */
    @GetMapping ("/pdf-view")
    public ResponseEntity<Resource> getPdf(
            @RequestParam("teacherId") Long teacherId,
            @RequestParam("filePath") String filePath
    ) throws IOException {
        String filepath = FileUtils.getDirectoryPath(String.valueOf(teacherId),filePath);
        System.out.println("文件路径"+filepath);
        File file = new File(filepath);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    /**
     * 获取指定课程信息
     * @param id
     * @return
     */
    @GetMapping("/getcurrentcourse")
    public Result getCurrentCourse(@RequestParam("id") String id) {
        return myService.getCourseById(id);
    }

    /**
     * 获取教师信息
     * @param id
     * @return
     */
    @GetMapping("/get-teacher")
    public Result getTeacher(@RequestParam("id") Long id){
        return myService.getTeacher(id);
    }
}
