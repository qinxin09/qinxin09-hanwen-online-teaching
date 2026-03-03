package com.yws.onlinestudymanage.dao;

import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.pojo.StudyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudyRecordMapper {
    long countByExample(StudyRecordExample example);

    int deleteByExample(StudyRecordExample example);

    int deleteByPrimaryKey(String recordId);

    int insert(StudyRecord record);

    int insertSelective(StudyRecord record);

    List<StudyRecord> selectByExampleWithBLOBs(StudyRecordExample example);

    List<StudyRecord> selectByExample(StudyRecordExample example);

    StudyRecord selectByPrimaryKey(String recordId);

    int updateByExampleSelective(@Param("record") StudyRecord record, @Param("example") StudyRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") StudyRecord record, @Param("example") StudyRecordExample example);

    int updateByExample(@Param("record") StudyRecord record, @Param("example") StudyRecordExample example);

    int updateByPrimaryKeySelective(StudyRecord record);

    int updateByPrimaryKeyWithBLOBs(StudyRecord record);

    int updateByPrimaryKey(StudyRecord record);
}