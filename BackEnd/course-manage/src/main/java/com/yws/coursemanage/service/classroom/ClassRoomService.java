package com.yws.coursemanage.service.classroom;

import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.CourseWithBLOBs;
import com.yws.coursemanage.utils.Result;

public interface ClassRoomService {
    Result createClassRoom(ClassRoom classRoom);

    Result updateClassRoom(ClassRoom classRoom);

    Result deleteClassRoom(ClassRoom classRoom);

    Result getMembersByClassroomId(String classroomId);

    Result getClassroomByClassroomId(String classroomId);
}
