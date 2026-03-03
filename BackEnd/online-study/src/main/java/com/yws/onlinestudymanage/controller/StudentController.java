package com.yws.onlinestudymanage.controller;

import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.pojo.custom.MyClassRoom;
import com.yws.onlinestudymanage.service.student.StudentService;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取学生所在班级
 * 加入班级
 * 退出班级
 * 更新学习记录
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    /**
     * 获取学生所在班级
     * @param studyRecord
     * @return
     */
    @PostMapping("/my-class-room")
    public Result getStudyRecord(@RequestBody StudyRecord studyRecord) {
        List<MyClassRoom> studyRecordList = studentService.getStudyRecordById(studyRecord.getUserId());
        if(studyRecordList.size()>0){
            return new Result().success("获取课堂成功!",studyRecordList);
        }
        return new Result().error("获取课堂失败！",studyRecordList);
    }

    /**
     * 加入班级
     * @param studyRecord
     * @return
     */
    @PostMapping("/join-class-room")
    public Result joinClassRoom(@RequestBody StudyRecord studyRecord) {
        return studentService.joinClassRoom(studyRecord);
    }

    /**
     * 退出班级
     * @param studyRecord
     * @return
     */
    @PostMapping("/quit-classroom")
    public Result quitClassRoom(@RequestBody StudyRecord studyRecord) {
        return studentService.quitClassRoom(studyRecord.getRecordId());
    }

    /**
     * 更新学习记录
     * @param studyRecord
     * @return
     */
    @PostMapping("/update-study-record")
    public Result updateStudyRecord(@RequestBody StudyRecord studyRecord) {
        return studentService.updateStudyRecord(studyRecord);
    }
}