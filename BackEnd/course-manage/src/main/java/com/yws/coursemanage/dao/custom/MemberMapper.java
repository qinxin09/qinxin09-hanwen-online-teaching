package com.yws.coursemanage.dao.custom;

import com.yws.coursemanage.pojo.custom.Member;

import java.util.List;

public interface MemberMapper {
    List<Member> getMembersByClassroomId(String classroomId);
}
