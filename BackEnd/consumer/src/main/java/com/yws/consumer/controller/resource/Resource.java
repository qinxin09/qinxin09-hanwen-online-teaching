package com.yws.consumer.controller.resource;

import com.yws.consumer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource")
public class Resource {
    @Autowired
    private RestTemplate restTemplate;

    private String origin = "http://" + "springcloud-resource-manage-8300";
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
//    创建文件夹
    @GetMapping("/create-folder")
    public Object createFolder(@RequestParam("dir") String dir, @RequestParam("userId") String userId , @RequestParam("name") String folderName){
        return this.doGet("/resource/create-folder?dir="+dir+"&userId="+userId+"&name="+folderName);
    }
//    删除路径
    @GetMapping("/delete-path")
    public Object deletePath(@RequestParam("dir") String dir,@RequestParam("userid") int userId){
        System.out.println(dir);
        return this.doGet("/resource/delete-path?dir="+dir+"&userid="+userId);
    }
//    重命名文件（夹）
    @GetMapping("/rename")
    public Object rename(@RequestParam("dir") String dir,@RequestParam("userid") int userId,@RequestParam("newname") String newName){
        return this.doGet("/resource/rename?dir="+dir+"&userid="+userId+"&newname="+newName);
    }
//    查询当前路径下的所有文件（夹）
    @GetMapping("/dir")
    public Object getDirectory(@RequestParam("dir") String dir,@RequestParam("userid") int userId) {
        return this.doGet("/resource/dir?dir="+dir+"&userid="+userId);
    }

}
