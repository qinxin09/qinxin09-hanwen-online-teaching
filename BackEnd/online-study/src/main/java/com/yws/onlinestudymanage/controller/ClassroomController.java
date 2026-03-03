package com.yws.onlinestudymanage.controller;

import com.yws.onlinestudymanage.dao.StudyRecordMapper;
import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.service.classroom.ClassroomService;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//获取指定班级里的所有学生的学习记录
@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    @Autowired
    ClassroomService classroomService ;

    /**
     * 获取指定班级里的所有学生的学习记录
     * @param classroomId 班级id
     * @return
     */
    @GetMapping("/get-classroom-studyrecord")
    public Result getClassroomStudyRecord(@RequestParam("classroomId") String classroomId){
        return classroomService.getClassroomAllStudyRecord(classroomId);
    }


}
