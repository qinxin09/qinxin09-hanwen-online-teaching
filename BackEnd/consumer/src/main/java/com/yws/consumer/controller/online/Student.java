package com.yws.consumer.controller.online;

import com.yws.consumer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class Student {
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
    @PostMapping("/my-class-room")
    public Object getStudyRecord(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/student/my-class-room",requestBody);
    }
    @PostMapping("/join-class-room")
    public Object joinClassRoom(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/student/join-class-room",requestBody);
    }
    @PostMapping("/quit-classroom")
    public Object quitClassRoom(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/student/quit-classroom",requestBody);
    }
    @PostMapping("/update-study-record")
    public Object updateStudyRecord(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/student/update-study-record",requestBody);
    }
}
