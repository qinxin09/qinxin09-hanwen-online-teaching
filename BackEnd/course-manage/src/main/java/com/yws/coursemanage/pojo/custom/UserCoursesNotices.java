package com.yws.coursemanage.pojo.custom;

import com.yws.coursemanage.pojo.Notice;

public class UserCoursesNotices extends Notice {
    private String courseName;
    private String classroomName;
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }


}
