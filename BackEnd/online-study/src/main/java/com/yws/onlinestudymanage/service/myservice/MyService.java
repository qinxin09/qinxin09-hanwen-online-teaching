package com.yws.onlinestudymanage.service.myservice;


import com.yws.onlinestudymanage.utils.Result;

public interface MyService {
    Result getCourseById(String courseId);

    Result getTeacher(Long teacherId);
}
