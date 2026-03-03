package com.yws.onlinestudymanage.service.classroom.impl;


import com.yws.onlinestudymanage.dao.StudyRecordMapper;
import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.pojo.StudyRecordExample;
import com.yws.onlinestudymanage.service.classroom.ClassroomService;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Resource
    StudyRecordMapper studyRecordMapper;

    @Override
    public Result getClassroomAllStudyRecord(String classroomId) {
        StudyRecordExample studyRecordExample = new StudyRecordExample();
        studyRecordExample.createCriteria().andClassroomIdEqualTo(classroomId);
        List<StudyRecord> studyRecordList =  studyRecordMapper.selectByExampleWithBLOBs(studyRecordExample);
        if(studyRecordList.size()>0){
            return new Result().success("ok",studyRecordList);
        }
        return new Result().error("error",null);
    }
}