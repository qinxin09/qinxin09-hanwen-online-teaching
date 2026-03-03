package com.yws.consumer.controller.online;

import com.yws.consumer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/online-study")
public class onlineStudy {
    @Autowired
    private RestTemplate restTemplate;

    private String origin = "http://" + "springcloud-online-study-8400";

    public Object doPost(String path, Map<String, Object> requestBody){
        String url = this.origin + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        Object obj = restTemplate.postForObject(url, requestEntity, Result.class);
        return obj;
    }
    public Object doGet(String path){
        String url = this.origin + path;
        return restTemplate.getForObject(url, Result.class);
    }

//    在线播放视频、在线预览pdf 特殊，这里用不了，使用网关吧
//    @GetMapping(value = "/video-play")
//    public Object playVideo(@RequestParam("teacherId") Long teacherId,@RequestParam("filePath") String filePath) {
//        return this.doGet("/online-study/video-play?teacherId="+teacherId+"&filePath="+filePath);
//    }
//    @GetMapping ("/pdf-view")
//    public Object getPdf(@RequestParam("teacherId") Long teacherId,@RequestParam("filePath") String filePath ){
//        return this.doGet("/online-study/pdf-view?teacherId="+teacherId+"&filePath="+filePath);
//    }


    @GetMapping("/getcurrentcourse")
    public Object getCurrentCourse(@RequestParam("id") String id) {
        return this.doGet("/online-study/getcurrentcourse?id="+id);
    }
    @GetMapping("/get-teacher")
    public Object getTeacher(@RequestParam("id") Long id){
        return this.doGet("/online-study/get-teacher?id="+id);
    }
}
