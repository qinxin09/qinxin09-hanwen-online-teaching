package com.yws.coursemanage.dao.custom;

import com.yws.coursemanage.pojo.custom.ClassroomWorks;
import com.yws.coursemanage.pojo.custom.TeacherCourseInfo;
import com.yws.coursemanage.pojo.custom.UserCoursesNotices;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyMapper {
    List<TeacherCourseInfo> getTeacherCreateCourse(Long teacherId);
    List<UserCoursesNotices> getUserCoursesNotices(Long userId);
    List<UserCoursesNotices> getTeacherCoursesNotices(Long teacherId);

    List<ClassroomWorks> getWorksByClassroomId(String classroomId);

    int insertClassroomWork(ClassroomWorks classroomWorks);

    ClassroomWorks selectClassroomWork(ClassroomWorks classroomWorks);

    int deleteClassroomWork(ClassroomWorks classroomWorks);

    int cleanClassroomWork(String workId);

//    ClassRoom checkClassRoomExistsByCourseIdTeacherId(ClassRoom classRoom);
//
//    int createClassRoom(ClassRoom classRoom);
}
