package com.yws.coursemanage.service.classroom.impl;

import com.yws.coursemanage.dao.ClassRoomMapper;
import com.yws.coursemanage.dao.custom.MemberMapper;
import com.yws.coursemanage.pojo.ClassRoom;
import com.yws.coursemanage.pojo.ClassRoomExample;
import com.yws.coursemanage.pojo.custom.Member;
import com.yws.coursemanage.service.classroom.ClassRoomService;
import com.yws.coursemanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {
    @Resource
    ClassRoomMapper classRoomMapper;
    @Resource
    MemberMapper memberMapper;

    @Override
    public Result createClassRoom(ClassRoom classRoom) {
        ClassRoomExample classRoomExample =new ClassRoomExample();
        ClassRoomExample.Criteria criteria = classRoomExample.createCriteria();
        criteria.andTeacherIdEqualTo(classRoom.getTeacherId());
        criteria.andCourseIdEqualTo(classRoom.getCourseId());
        criteria.andNameEqualTo(classRoom.getName());
        List<ClassRoom> classRoomList = classRoomMapper.selectByExample(classRoomExample);
        if(classRoomList.size()==0){
            if(classRoomMapper.insert(classRoom)>0){
                classRoomList = classRoomMapper.selectByExample(classRoomExample);
                return new Result().success("创建成功",classRoomList.get(0));
            }
            return new Result().error("创建失败",null);
        }else{
            return new Result().error("班级已存在",null);
        }
    }

    @Override
    public Result updateClassRoom(ClassRoom classRoom) {
        ClassRoomExample classRoomExample = new ClassRoomExample();
        ClassRoomExample.Criteria criteria = classRoomExample.createCriteria();
        criteria.andTeacherIdEqualTo(classRoom.getTeacherId());
        criteria.andCourseIdEqualTo(classRoom.getCourseId());
        criteria.andNameEqualTo(classRoom.getName());
        List<ClassRoom> classRoomList = classRoomMapper.selectByExample(classRoomExample);
        if(classRoomList.size()>0){
            return new Result().error("更新失败，已存在班级["+classRoom.getName()+"]",null);
        }
        if(classRoomMapper.updateByPrimaryKey(classRoom)>0){
            return new Result().success("更新成功",null);
        }
        return new Result().error("更新失败",null);
    }

    @Override
    public Result deleteClassRoom(ClassRoom classRoom) {
        if(classRoomMapper.deleteByPrimaryKey(classRoom.getClassroomId())>0){
            return new Result().success("删除班级成功！",null);
        }
        return new Result().error("删除班级失败！",null);
    }

    @Override
    public Result getMembersByClassroomId(String classroomId) {
        List<Member> memberList = memberMapper.getMembersByClassroomId(classroomId);
        if(memberList.size()>0){
            return new Result().success("查询成员成功!",memberList);
        }
        return new Result().error("查询成员失败!",memberList);
    }

    @Override
    public Result getClassroomByClassroomId(String classroomId) {
        ClassRoom classRoom = classRoomMapper.selectByPrimaryKey(classroomId);
        if(classRoom!=null){
            return new Result().success("ok",classRoom);
        }
        return new Result().error("error",null);
    }
}
