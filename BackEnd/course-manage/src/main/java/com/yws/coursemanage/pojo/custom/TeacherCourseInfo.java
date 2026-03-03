package com.yws.coursemanage.pojo.custom;

import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.Course;
import com.yws.coursemanage.pojo.CourseWithBLOBs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class TeacherCourseInfo extends CourseWithBLOBs {
    public List<ClassRoom> getClassRoomList() {
        return classRoomList;
    }

    public void setClassRoomList(List<ClassRoom> classRoomList) {
        this.classRoomList = classRoomList;
    }

    List<ClassRoom> classRoomList;


}
