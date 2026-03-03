import { Interface } from "@/global";
import axios from "axios";

const common_data={
    namespaced:true,
    actions:{
        
        initMembers(context,data){
            // 获取课程成员
            context.commit('SET_MEMBERS',[]);
            let url=Interface.teacher.get_members+"?classroomId="+data.data.classroomId;
            axios.get(url).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit('SET_MEMBERS',res.data.data);
                    }else{
                        data.callback.error(res.data.msg);
                    }
                },
                error=>{
                    Vue.prototype.$message.error(error.message);
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        initClassroomAllRecord(context,data){
            axios.get(Interface.online_study.get_classroom_studyrecord+"?classroomId="+data.data.classroomId).then(
                res=>{
                  if(res.data.code==200){
                    data.callback.success(res.data.msg);
                    context.commit("INIT_CLASSROOM_RECORDS",res.data.data);
                  }else{
                    console.log("获取班级所有学习记录失败")
                    context.commit("INIT_CLASSROOM_RECORDS",[]);
                  }
                },
                error=>{
                  console.error(error);
                }
              ).catch(e=>{
                  console.error(e);
                }
              )
        },
        ininCourse(context,data){
            axios.get(Interface.online_study.get_current_course+"?id="+data.data.courseId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg,res.data.data.teacherId);
                        context.commit("INIT_COURSE",res.data.data);
                    }else if(res.data.code==400){
                        data.callback.error(res.data.msg);
                    }
                },
                err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        initTeacher(context,data){
            axios.get(Interface.online_study.get_teacher+"?id="+data.data.teacherId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        context.commit("INIT_TEACHER",res.data.data);
                    }else {
                        data.callback.error(res.data.msg);
                    }
                },
                error=>{console.log(error);}
            ).catch(e=>{
                console.error(e);
            })
        },
        initClassroom(context,data){
            axios.get(Interface.teacher.get_classroom+"?classroomId="+data.data.classroomId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        context.commit("INIT_CLASSROOM",res.data.data);
                    }
                },error=>{console.log(error);
                }
            ).catch(e=>{
                console.error(e);
            })
        },
        initClassroomWorks(context,data){
            axios.get(Interface.student.get_classroomworks+"?classroomId="+data.data.classroomId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        context.commit("INIT_CLASSROOMWORKS",res.data.data);
                    }else{
                        // console.error(res.data.msg);
                        // 没有发布作业
                    }
                },
                error=>{
                    console.error(error);
                }
            ).catch(e=>{
                console.error(e);
            })
        }
    
    },
    mutations:{
        SET_MEMBERS(state,data){
            state.members=data;
        },
        INIT_CLASSROOM_RECORDS(state,data){
            state.classroom_studyrecord=data;
        },
        INIT_COURSE(state,value){
            state.course=value;
        },
        INIT_TEACHER(state,value){
            state.teacher=value;
        },
        INIT_CLASSROOM(state,value){
            state.classroom=value;
        },
        INIT_CLASSROOMWORKS(state,value){
            state.classroom_works=value;
        }
        
    },
    getters:{
        chapters(state){
            return state.course.chapters;
        },
        getUserNameById(state){
            return (userId)=>{
                let name="null";
                state.members.some(member => {
                    if(member.id==userId){
                        name = member.name;
                        //退出循环
                        return true;
                    }
                });
                return name;
            }
        }
    },
    state:{
        members:[],
        classroom_studyrecord:[],
        classroom_works:[],//发布到班级里的作业
        course:{},
        teacher:{},
        classroom:{}
    }
}
export default common_data;