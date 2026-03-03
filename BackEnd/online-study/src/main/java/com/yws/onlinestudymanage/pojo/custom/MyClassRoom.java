package com.yws.onlinestudymanage.pojo.custom;

import com.yws.onlinestudymanage.pojo.StudyRecord;

public class MyClassRoom extends StudyRecord {
    String courseName;
    String courseId;
    String chapters;

    String classroomName;

    String teacherName;
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

    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }
}
