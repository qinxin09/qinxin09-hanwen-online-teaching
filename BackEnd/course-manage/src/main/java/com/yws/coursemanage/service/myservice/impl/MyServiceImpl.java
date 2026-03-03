package com.yws.coursemanage.service.myservice.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.yws.coursemanage.dao.custom.MyMapper;
import com.yws.coursemanage.pojo.custom.ClassroomWorks;
import com.yws.coursemanage.pojo.custom.TeacherCourseInfo;
import com.yws.coursemanage.service.myservice.MyService;
import com.yws.coursemanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {
    @Resource
    MyMapper myMapper;


    @HystrixCommand(commandProperties ={
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD,
                    value = "10"),//10秒内请求数大于10个就启动熔断器，当请求符合熔断条件触发fallbackMethod 默认20个
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE,
                    value = "50"),//请求错误率大于50%自动熔断，然后for循环发起重试请求，当请求符合熔断条件触发fallbackMethod
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS,
                    value = "5000")//熔断多少秒后去重试请求
    },fallbackMethod = "FallBack")
    @Override
    public Result getTeacherCreateCourse(Long teacherId) {
        try{
            List<TeacherCourseInfo> list = myMapper.getTeacherCreateCourse(teacherId);
            return new Result().success("查询教师创建的课程成功",list);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("教师获取课程失败！");
        }

    }
    public Result FallBack(Long id) {
        System.out.println("托底程序, 教师获取课程失败！");
        return new Result().error("教师获取课程失败",null);
    }

    @Override
    public Result getUserCoursesNotices(Long userId) {
        return new Result().success("ok",myMapper.getUserCoursesNotices(userId));
    }

    @Override
    public Result getWorksByClassroomId(String classroomId) {
        List<ClassroomWorks> classroomWorksList = myMapper.getWorksByClassroomId(classroomId);
        if(classroomWorksList.size()>0){
            return new Result().success("ok",classroomWorksList);
        }
        return new Result().error("error",null);
    }

    @Override
    public Result setWorks(ClassroomWorks classroomWorks) {
        if(myMapper.selectClassroomWork(classroomWorks)!=null){
            return new Result().error("已经发布！",classroomWorks);
        }
        if(myMapper.insertClassroomWork(classroomWorks)>0){
            return new Result().success("发布成功",null);
        }
        return new Result().error("发布失败",null);
    }

    @Override
    public Result revokeWorks(ClassroomWorks classroomWorks) {
        if(myMapper.selectClassroomWork(classroomWorks)!=null){
            if(myMapper.deleteClassroomWork(classroomWorks)>0){
                return new Result().success("撤回成功",null);
            }
        }

        return new Result().error("撤回失败",null);
    }

    @Override
    public Result cleanClassroomWorks(String workId) {
        if (myMapper.cleanClassroomWork(workId)>0){
            return new Result().success("删除成功!",null);
        }
        return new Result().success("删除失败!",null);
    }

    @Override
    public Result getTeacherCoursesNotices(Long teacherId) {
        return new Result().success("ok",myMapper.getTeacherCoursesNotices(teacherId));
    }
}
