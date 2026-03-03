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
@RequestMapping("/course")
public class Course {
    @Autowired
    private RestTemplate restTemplate;

    private String origin = "http://" + "springcloud-course-manage-8200";
    public Object doPost(String path,Map<String, Object> requestBody){
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
    @PostMapping(value = "/create")
    public Object createCourse(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/create",requestBody);
    }
    @PostMapping(value = "/delete")
    public Object deleteCourse(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/delete",requestBody);
    }
    @PostMapping("/mycreatecourse")
    public Object myCreateCourses(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/mycreatecourse",requestBody);
    }
    @PostMapping("/mycreateclassroom")
    public Object myCreateClassRoom(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/mycreateclassroom",requestBody);
    }
    @PostMapping("/updateclassroom")
    public Object updateClassRoom(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/updateclassroom",requestBody);
    }
    @PostMapping("/deleteclassroom")
    public Object deleteClassRoom(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/deleteclassroom",requestBody);
    }
    @PostMapping("/savechapters")
    public Object saveChapters(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/savechapters",requestBody);
    }
    @PostMapping("/getnotices")
    public Object getNotices(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/getnotices",requestBody);
    }
    @PostMapping("/createnotice")
    public Object createNotice(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/createnotice",requestBody);
    }
    @PostMapping("/updatenotice")
    public Object updateNotice(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/updatenotice",requestBody);
    }

    @PostMapping("/deletenotice")
    public Object deleteNotice(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/deletenotice",requestBody);
    }
    @GetMapping("/getusernotices")
    public Object getUserCoursesNotices(@RequestParam("userId") Long userId) {
        return this.doGet("/course/getusernotices?userId="+userId);
    }

    //获取教师创建的所有公告
    @GetMapping("/get-teacher-notices")
    public Object getTeacherCoursesNotices(@RequestParam("teacherId") Long teacherId) {
        return this.doGet("/course/get-teacher-notices?teacherId="+teacherId);
    }
    @PostMapping("/savehomework")
    public Object saveHomework(@RequestBody Map<String, Object> requestBody) {
        return this.doPost("/course/savehomework",requestBody);
    }
}