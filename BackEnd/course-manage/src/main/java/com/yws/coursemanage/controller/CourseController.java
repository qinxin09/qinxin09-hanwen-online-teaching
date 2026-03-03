package com.yws.coursemanage.controller;

import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.Course;
import com.yws.coursemanage.pojo.CourseWithBLOBs;
import com.yws.coursemanage.pojo.Notice;
import com.yws.coursemanage.service.classroom.ClassRoomService;
import com.yws.coursemanage.service.course.CourseService;
import com.yws.coursemanage.service.myservice.MyService;
import com.yws.coursemanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 创建课程
 * 删除指定课程
 * 获取教师创建的课程
 * 获取创建的班级
 * 更新指定班级信息
 * 删除指定班级
 * 保存章节信息
 * 获取公告信息
 * 创建公告
 * 更新公告信息
 * 删除公告信息
 * 获取教师创建的所有公告
 */

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    ClassRoomService classRoomService;
    @Autowired
    MyService myService;

    /**
     * 创建课程
     * @param course
     * @return
     */
    @PostMapping("/create")
    public Result createCourse(@RequestBody CourseWithBLOBs course){
        return courseService.createCourse(course);
    }

    /**
     * 删除指定课程
     * @param course
     * @return
     */
    @PostMapping("/delete")
    public Result deleteCourse(@RequestBody Course course){
        return courseService.deleteCourse(course);
    }

    /**
     * 获取教师创建的课程
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/mycreatecourse")
    public Result myCreateCourses(@RequestBody Map<String, Object> requestBody) {
        Integer id = (Integer) requestBody.get("id");
//        return courseService.getMyCreateCourse(Long.valueOf(id));
        System.out.println("查询我创建的课程,my id="+id);
        return myService.getTeacherCreateCourse(Long.valueOf(id));
    }


    /**
     * 获取创建的班级
     * @param classRoom
     * @return
     */
    @PostMapping("/mycreateclassroom")
    public Result myCreateClassRoom(@RequestBody ClassRoom classRoom) {
        return classRoomService.createClassRoom(classRoom);
    }


    /**
     * 更新指定班级信息
     * @param classRoom
     * @return
     */
    @PostMapping("/updateclassroom")
    public Result updateClassRoom(@RequestBody ClassRoom classRoom) {
        return classRoomService.updateClassRoom(classRoom);
    }

    /**
     * 删除指定班级
     * @param classRoom
     * @return
     */
    @PostMapping("/deleteclassroom")
    public Result deleteClassRoom(@RequestBody ClassRoom classRoom) {
        return classRoomService.deleteClassRoom(classRoom);
    }

    /**
     * 保存章节信息
     * @param course
     * @return
     */
    @PostMapping("/savechapters")
    public Result saveChapters(@RequestBody CourseWithBLOBs course) {
        return courseService.saveChaptersById(course);
    }

    /**
     * 获取公告信息
     * @param notice
     * @return
     */
    @PostMapping("/getnotices")
    public Result getNotices(@RequestBody Notice notice) {
        return courseService.getCourseNotices(notice);
    }

    /**
     * 创建公告
     * @param notice
     * @return
     */
    @PostMapping("/createnotice")
    public Result createNotice(@RequestBody Notice notice) {
        return courseService.createNotice(notice);
    }

    /**
     * 更新公告信息
     * @param notice
     * @return
     */
    @PostMapping("/updatenotice")
    public Result updateNotice(@RequestBody Notice notice) {
        return courseService.updateNotice(notice);
    }

    /**
     * 删除公告信息
     * @param notice
     * @return
     */
    @PostMapping("/deletenotice")
    public Result deleteNotice(@RequestBody Notice notice) {
        return courseService.deleteNotice(notice);
    }

    /**
     * 获取学生所在的所有班级的所有公告
     * @param userId
     * @return
     */
    @GetMapping("/getusernotices")
    public Result getUserCoursesNotices(@RequestParam("userId") Long userId) {
        return myService.getUserCoursesNotices(userId);
//        return new Result().error("test",userId);
    }

    /**
     * 获取教师创建的所有公告
     * @param teacherId 教师id
     * @return
     */
    @GetMapping("/get-teacher-notices")
    public Result getTeacherCoursesNotices(@RequestParam("teacherId") Long teacherId) {
        return myService.getTeacherCoursesNotices(teacherId);
//        return new Result().error("test",userId);
    }

    /**
     * 保存创建的作业
     * @param course
     * @return
     */
    @PostMapping("/savehomework")
    public Result saveHomework(@RequestBody CourseWithBLOBs course) {
        return courseService.saveHomework(course);
    }
}
