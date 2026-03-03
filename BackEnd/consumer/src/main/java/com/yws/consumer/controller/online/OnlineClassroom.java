package com.yws.consumer.controller.online;

import com.yws.consumer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/classroom")
public class OnlineClassroom {
    @Autowired
    private RestTemplate restTemplate;

    private String origin = "http://" + "springcloud-online-study-8400";
//    public Object doPost(String path, Map<String, Object> requestBody){
//        String url = this.origin + path;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//        Object obj = restTemplate.postForObject(url, requestEntity, Result.class);
//        return obj;
//    }
    public Object doGet(String path){
        String url = this.origin + path;
        return restTemplate.getForObject(url, Result.class);
    }

    @GetMapping("/get-classroom-studyrecord")
    public Object getClassroomStudyRecord(@RequestParam("classroomId") String classroomId){
        return doGet("/classroom/get-classroom-studyrecord?classroomId="+classroomId);
    }
}
