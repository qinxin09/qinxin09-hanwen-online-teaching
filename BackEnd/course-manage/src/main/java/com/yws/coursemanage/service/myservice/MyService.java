package com.yws.coursemanage.service.myservice;

import com.yws.coursemanage.pojo.custom.ClassroomWorks;
import com.yws.coursemanage.utils.Result;

public interface MyService {
    Result getTeacherCreateCourse(Long teacherId);
    Result getUserCoursesNotices(Long userId);

    Result getWorksByClassroomId(String classroomId);

    Result setWorks(ClassroomWorks classroomWorks);

    Result revokeWorks(ClassroomWorks classroomWorks);

    Result cleanClassroomWorks(String workId);

    Result getTeacherCoursesNotices(Long teacherId);

}
