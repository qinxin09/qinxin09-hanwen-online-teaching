package com.yws.onlinestudymanage.service.myservice.impl;

import com.yws.onlinestudymanage.dao.custom.MyClassRoomMapper;
import com.yws.onlinestudymanage.pojo.custom.MyCourse;
import com.yws.onlinestudymanage.service.myservice.MyService;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MyServiceImpl implements MyService {

    @Resource
    MyClassRoomMapper myClassRoomMapper ;
    @Override
    public Result getCourseById(String courseId) {
        MyCourse myCourse = myClassRoomMapper.getCourseById(courseId);
        if(myCourse!=null){
            return new Result().success("ok",myCourse);
        }
        return new Result().error("error",null);
    }

    @Override
    public Result getTeacher(Long teacherId) {
        return new Result().success("ok",myClassRoomMapper.getTeacherById(teacherId));
    }
}
