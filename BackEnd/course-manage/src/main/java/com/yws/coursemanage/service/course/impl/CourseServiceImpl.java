package com.yws.coursemanage.service.course.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yws.coursemanage.dao.ClassRoomMapper;
import com.yws.coursemanage.dao.CourseMapper;
import com.yws.coursemanage.dao.NoticeMapper;
import com.yws.coursemanage.pojo.*;
import com.yws.coursemanage.service.course.CourseService;
import com.yws.coursemanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    CourseMapper courseMapper;

    @Resource
    ClassRoomMapper classRoomMapper;
    @Resource
    NoticeMapper noticeMapper;
    @Override
    public Result createCourse(CourseWithBLOBs course) {
        try{
            System.out.println(new ObjectMapper().writeValueAsString(course));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(courseMapper.insert(course) > 0){
            return new Result().success("创建课程成功",null);
        }
        return new Result().error("创建课程失败",null);
    }

    @Override
    public Result getMyCreateCourse(Long id) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        List<Course> courseList = courseMapper.selectByExample(courseExample);
        return new Result().success("查询成功",courseList);
    }

    @Override
    public Result deleteCourse(Course course) {
        ClassRoomExample classRoomExample = new ClassRoomExample();
        classRoomExample.createCriteria().andCourseIdEqualTo(course.getId());
        classRoomMapper.deleteByExample(classRoomExample);
        if(courseMapper.deleteByPrimaryKey(course.getId())>0){
            return new Result().success("删除成功",null);
        }
        return new Result().error("删除失败",null);
    }

    @Override
    public Result saveChaptersById(CourseWithBLOBs course) {
        CourseWithBLOBs targetCourse = courseMapper.selectByPrimaryKey(course.getId());
        if(targetCourse!=null){
            targetCourse.setChapters(course.getChapters());
            courseMapper.updateByPrimaryKeyWithBLOBs(targetCourse);
            return new Result().success("保存成功！",null);
        }
        return new Result().error("保存失败！",null);
    }

    @Override
    public Result createNotice(Notice notice) {
        if(noticeMapper.selectByPrimaryKey(notice.getId())==null){
            if(noticeMapper.insert(notice)>0){
                return new Result().success("保存公告成功！",null);
            }
        }
        return new Result().error("创建公告失败！",notice);
    }

    @Override
    public Result updateNotice(Notice notice) {
        if(noticeMapper.selectByPrimaryKey(notice.getId())!=null){
            if(noticeMapper.updateByPrimaryKey(notice)>0){
                return new Result().success("修改公告成功！",null);
            }
        }
        return new Result().error("修改公告失败！",null);
    }

    @Override
    public Result deleteNotice(Notice notice) {
        if(noticeMapper.deleteByPrimaryKey(notice.getId())>0){
            return new Result().success("删除公告成功",null);
        }
        return new Result().error("删除公告失败",null);
    }

    @Override
    public Result getCourseNotices(Notice notice) {
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.createCriteria().andCourseIdEqualTo(notice.getCourseId());
        List<Notice> noticeList = noticeMapper.selectByExample(noticeExample);
        if(noticeList.size()>0){
            return new Result().success("查询公告成功!",noticeList);
        }
        return new Result().error("查询公告失败!",null);
    }

    @Override
    public Result saveHomework(CourseWithBLOBs course) {
        CourseWithBLOBs targetCourse = courseMapper.selectByPrimaryKey(course.getId());
        if(targetCourse!=null){
            targetCourse.setWorks(course.getWorks());
            if(courseMapper.updateByPrimaryKeyWithBLOBs(targetCourse)>0){
                return new Result().success("更新成功！",null);
            }
        }
        return new Result().error("更新失败！",null);
    }

}
