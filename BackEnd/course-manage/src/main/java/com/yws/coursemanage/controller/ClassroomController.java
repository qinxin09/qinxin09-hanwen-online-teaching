package com.yws.coursemanage.controller;

import com.yws.coursemanage.pojo.CourseWithBLOBs;
import com.yws.coursemanage.pojo.custom.ClassroomWorks;
import com.yws.coursemanage.pojo.custom.Member;
import com.yws.coursemanage.service.classroom.ClassRoomService;
import com.yws.coursemanage.service.myservice.MyService;
import com.yws.coursemanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * classroom
 * 获取班级成员
 * 获取发布到班级里的作业
 * 发布作业到班级
 * 删除作业
 * 撤回发布的作业
 * 获取指定班级信息
 *
 */
@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    @Autowired
    ClassRoomService classRoomService;
    @Autowired
    MyService myService;
    @GetMapping("/getmembers")
    public Result getMembers(@RequestParam("classroomId") String classroomId){
        return classRoomService.getMembersByClassroomId(classroomId);
    }
//    获取发布到班级里的作业
    @GetMapping("/getclassroomworks")
    public Result getWorks(@RequestParam("classroomId") String classroomId){
        return myService.getWorksByClassroomId(classroomId);
    }
//    发布作业到班级
    @PostMapping("/setclassroom-work")
    public Result setWorks(@RequestBody ClassroomWorks classroomWorks){
        return myService.setWorks(classroomWorks);
    }
//    在某课程的所有班级里，删除指定作业
    @GetMapping("/cleanclassroom-works")
    public Result cleanClassroomWorks(@RequestParam("workId") String workId){
        return myService.cleanClassroomWorks(workId);
    }
//    撤回发布到指定班级里的指定作业
    @PostMapping("/revokeclassroom-work")
    public Result revokeWorks(@RequestBody ClassroomWorks classroomWorks){
        return myService.revokeWorks(classroomWorks);
    }
//    获取指定班级信息
    @GetMapping("/get-classroom")
    public Result getClassroomByClassroomId(@RequestParam("classroomId") String classroomId){
        return classRoomService.getClassroomByClassroomId(classroomId);
    }
}
