package com.yws.onlinestudymanage.service.student;

import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.pojo.custom.MyClassRoom;
import com.yws.onlinestudymanage.utils.Result;

import java.util.List;

public interface StudentService {
    List<MyClassRoom> getStudyRecordById(Long id);

    Result joinClassRoom(StudyRecord studyRecord);

    Result quitClassRoom(String recordId);

    Result updateStudyRecord(StudyRecord studyRecord);
}
