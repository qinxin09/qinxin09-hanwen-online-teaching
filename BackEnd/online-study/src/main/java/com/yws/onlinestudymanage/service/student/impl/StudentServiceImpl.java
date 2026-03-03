package com.yws.onlinestudymanage.service.student.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.yws.onlinestudymanage.dao.StudyRecordMapper;
import com.yws.onlinestudymanage.dao.custom.MyClassRoomMapper;
import com.yws.onlinestudymanage.pojo.StudyRecord;
import com.yws.onlinestudymanage.pojo.StudyRecordExample;
import com.yws.onlinestudymanage.pojo.custom.MyClassRoom;
import com.yws.onlinestudymanage.service.student.StudentService;
import com.yws.onlinestudymanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    StudyRecordMapper studyRecordMapper;

    @Resource
    MyClassRoomMapper myClassRoomMapper;

    @Override
    public List<MyClassRoom> getStudyRecordById(Long id) {
        return myClassRoomMapper.getMyClassRoomsByUserId(id);
    }

    @Override
    public Result joinClassRoom(StudyRecord studyRecord) {
        StudyRecordExample studyRecordExample = new StudyRecordExample();
        StudyRecordExample.Criteria criteria = studyRecordExample.createCriteria();
        criteria.andClassroomIdEqualTo(studyRecord.getClassroomId());
        criteria.andUserIdEqualTo(studyRecord.getUserId());
        if(studyRecordMapper.selectByExample(studyRecordExample).size()==0){
            if(studyRecordMapper.insert(studyRecord)>0){
                return new Result().success("加入课堂成功！",null);
            }
        }else{
            return new Result().error("您已加入课堂！",null);
        }
        return new Result().error("加入课堂失败！",null);
    }

    @Override
    public Result quitClassRoom(String recordId) {
        if(studyRecordMapper.deleteByPrimaryKey(recordId)>0){
            return new Result().success("退出班级课堂成功！",null);
        }
        return new Result().error("退出班级课堂失败！",null);
    }

    @HystrixCommand(commandProperties ={
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD,
                    value = "10"),//10秒内请求数大于10个就启动熔断器，当请求符合熔断条件触发fallbackMethod 默认20个
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE,
                    value = "50"),//请求错误率大于50%自动熔断，然后for循环发起重试请求，当请求符合熔断条件触发fallbackMethod
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS,
                    value = "5000")//熔断多少秒后去重试请求
    },fallbackMethod = "FallBack")
    @Override
    public Result updateStudyRecord(StudyRecord studyRecord) {
        StudyRecord record = studyRecordMapper.selectByPrimaryKey(studyRecord.getRecordId());
        if(record!=null){
            record.setStudyRecord(studyRecord.getStudyRecord());
            if(studyRecordMapper.updateByPrimaryKeyWithBLOBs(record)>0){
                return new Result().success("更新成功",null);
            }
        }
        throw new RuntimeException("服务器宕机，请查看维护！");
    }
    public Result FallBack(StudyRecord studyRecord) {
        System.out.println("托底程序, 更新学习记录失败！");
        return new Result().error("更新学习记录失败",null);
    }
}
