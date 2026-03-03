package com.yws.coursemanage.service.course;

import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.Course;
import com.yws.coursemanage.pojo.CourseWithBLOBs;
import com.yws.coursemanage.pojo.Notice;
import com.yws.coursemanage.utils.Result;

public interface CourseService {
    Result createCourse(CourseWithBLOBs course);

    Result getMyCreateCourse(Long id);

    Result deleteCourse(Course course);
    Result saveChaptersById(CourseWithBLOBs course);

    Result createNotice(Notice notice);

    Result updateNotice(Notice notice);

    Result deleteNotice(Notice notice);

    Result getCourseNotices(Notice notice);

    Result saveHomework(CourseWithBLOBs course);
}
