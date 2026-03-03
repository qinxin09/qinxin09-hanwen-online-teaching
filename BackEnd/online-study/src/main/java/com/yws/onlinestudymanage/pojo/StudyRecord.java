package com.yws.onlinestudymanage.pojo;

import java.util.Date;

public class StudyRecord {
    private String recordId;

    private Long userId;

    private String classroomId;

    private Date joinDatetime;

    private String studyRecord;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId == null ? null : classroomId.trim();
    }

    public Date getJoinDatetime() {
        return joinDatetime;
    }

    public void setJoinDatetime(Date joinDatetime) {
        this.joinDatetime = joinDatetime;
    }

    public String getStudyRecord() {
        return studyRecord;
    }

    public void setStudyRecord(String studyRecord) {
        this.studyRecord = studyRecord == null ? null : studyRecord.trim();
    }
}