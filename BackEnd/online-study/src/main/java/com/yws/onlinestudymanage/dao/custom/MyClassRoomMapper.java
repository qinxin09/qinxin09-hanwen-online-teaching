package com.yws.onlinestudymanage.dao.custom;

import com.yws.onlinestudymanage.pojo.custom.MyClassRoom;
import com.yws.onlinestudymanage.pojo.custom.MyCourse;
import com.yws.onlinestudymanage.pojo.custom.MyTeacher;

import java.util.List;

public interface MyClassRoomMapper {
    List<MyClassRoom> getMyClassRoomsByUserId(Long id);
    MyCourse getCourseById(String courseId);

    MyTeacher getTeacherById(Long teacherId);
}
