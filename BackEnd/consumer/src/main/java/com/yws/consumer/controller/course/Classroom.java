package com.yws.consumer.controller.course;

import com.yws.consumer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/classroom")
public class Classroom {
    @Autowired
    private RestTemplate restTemplate;
    private String origin = "http://" + "springcloud-course-manage-8200";
    public Object doGet(String path){
        String url = this.origin + path;
        return restTemplate.getForObject(url, Result.class);
    }
    public Object doPost(String path, Map<String, Object> requestBody){
        String url = this.origin + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        Object obj = restTemplate.postForObject(url, requestEntity, Result.class);
        return obj;
    }

    @GetMapping("/getmembers")
    public Object getMembers(@RequestParam("classroomId") String classroomId){
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/getmembers" +"?classroomId="+classroomId;
//        Object obj = restTemplate.getForObject(url, Result.class);
//        return obj;
        return this.doGet("/classroom/getmembers" +"?classroomId="+classroomId);
    }
    //    获取发布到班级里的作业
    @GetMapping("/getclassroomworks")
    public Object getWorks(@RequestParam("classroomId") String classroomId){
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/getclassroomworks" +"?classroomId="+classroomId;
//        Object obj = restTemplate.getForObject(url, Result.class);
//        return obj;
        return this.doGet("/classroom/getclassroomworks" +"?classroomId="+classroomId);
    }
    //    发布作业到班级
    @PostMapping("/setclassroom-work")
    public Object setWorks(@RequestBody Map<String, Object> requestBody) {
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/setclassroom-work" ; //访问/course/mycreatecourse
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//        Object obj = restTemplate.postForObject(url, requestEntity, Result.class);
//        return obj;
        return this.doPost("/classroom/setclassroom-work",requestBody);
    }
    //    清空记录
    @GetMapping("/cleanclassroom-works")
    public Object cleanClassroomWorks(@RequestParam("workId") String workId){
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/cleanclassroom-works" +"?workId="+workId;
//        Object obj = restTemplate.getForObject(url, Result.class);
//        return obj;
        return this.doGet("/classroom/cleanclassroom-works" +"?workId="+workId);
    }
    //    撤回发布到班级里的作业
    @PostMapping("/revokeclassroom-work")
    public Object revokeWorks(@RequestBody Map<String, Object> requestBody) {
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/revokeclassroom-work" ; //访问/course/mycreatecourse
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//        Object obj = restTemplate.postForObject(url, requestEntity, Result.class);
//        return obj;
        return this.doPost("/classroom/revokeclassroom-work",requestBody);
    }

    @GetMapping("/get-classroom")
    public Object getClassroomByClassroomId(@RequestParam("classroomId") String classroomId){
//        String url = "http://" + "springcloud-course-manage-8200" + "/classroom/get-classroom" +"?classroomId="+classroomId;
//        Object obj = restTemplate.getForObject(url, Result.class);
//        return obj;
        return this.doGet("/classroom/get-classroom" +"?classroomId="+classroomId);
    }
}
