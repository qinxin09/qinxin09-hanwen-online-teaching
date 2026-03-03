package com.yws.coursemanage.dao;

import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.ClassRoomExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClassRoomMapper {
    long countByExample(ClassRoomExample example);

    int deleteByExample(ClassRoomExample example);

    int deleteByPrimaryKey(String classroomId);

    int insert(ClassRoom record);

    int insertSelective(ClassRoom record);

    List<ClassRoom> selectByExample(ClassRoomExample example);

    ClassRoom selectByPrimaryKey(String classroomId);

    int updateByExampleSelective(@Param("record") ClassRoom record, @Param("example") ClassRoomExample example);

    int updateByExample(@Param("record") ClassRoom record, @Param("example") ClassRoomExample example);

    int updateByPrimaryKeySelective(ClassRoom record);

    int updateByPrimaryKey(ClassRoom record);
}