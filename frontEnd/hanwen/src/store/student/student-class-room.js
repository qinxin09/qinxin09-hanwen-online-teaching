import {Interface} from "@/global";
import axios from 'axios'
import Vue from "vue";
const student_class_room={
    namespaced:true,
    actions:{
        ininCourse(context,data){
            axios.get(Interface.online_study.get_current_course+"?id="+data.data.courseId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
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
        init(context,data){
            axios.post(Interface.student.my_class_room,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        context.commit("INIT_CLASSROOM",res.data.data);
                    }else if(res.data.code==400){
                        data.callback.error(res.data.msg);
                        context.commit("INIT_CLASSROOM",[]);
                    }
                },
                err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        joinClassRoom(context,data){
            axios.post(Interface.student.join_class_room,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("JOIN_CLASSROOM",JSON.parse(JSON.stringify(data.data)));
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
        quit_classroom(context,data){
            axios.post(Interface.student.quit_classroom,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("QUIT_CLASSROOM",JSON.parse(JSON.stringify(data.data)));
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
        updateStudyRecord(context,data){
            axios.post(Interface.student.update_study_record,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("UPDATE_STUDY_RECORD",JSON.parse(JSON.stringify(data.data)));
                    }else if(res.data.code==400){
                        data.callback.error(res.data.msg);
                    }
                },err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        }
    },
    mutations:{
        INIT_CLASSROOM(state,value){
            console.log("初始化课堂信息");
            // state.myCreate.splice(0,state.myCreate.length,...value);
            state.classrooms.splice(0,state.classrooms.length,...value);
            Vue.prototype.$bus.$emit("init-classroom-ok");
        },
        QUIT_CLASSROOM(state,value){
            for(let i=0;i<state.classrooms.length;i++){
                if(state.classrooms[i].recordId==value.recordId){
                    state.classrooms.splice(i,1);
                    break;
                }
            }
        },
        JOIN_CLASSROOM(state,value){
            //callback.success已采用刷新手段处理
        },
        UPDATE_STUDY_RECORD(state,value){
            for(let i=0;i<state.classrooms.length;i++){
                if(state.classrooms[i].recordId==value.recordId){
                    state.classrooms[i].studyRecord=value.studyRecord;
                    return ;
                }
            }
        },
        INIT_COURSE(state,value){
            state.course=value;
        }
        
    },
    getters:{
        getClassroom(state){
            return state.classrooms;
        },
        getClassroomById(state){
            return (id)=>{
                for(let i=0;i<state.classrooms.length;i++){
                    if(state.classrooms[i].courseId==id){
                        return state.classrooms[i];
                    }
                }
                return null;
            }
        },
        getCourse(state){
            return state.course;
        },
        getHomeworks(state){
            return (option)=>{
                if(state.course.works.length==0){
                    return [];
                }
                //根据课程id拿到所有的作业
                let homeworks=JSON.parse(state.course.works+"");
                // 根据班级id拿到班级下的所有作业
                let classroomWorks=option.classroomWorks;
                option.tableData.splice(0);
                // console.log(classroomWorks,homeworks);
                for(let i=0;i<classroomWorks.length;i++){
                    for(let j=0;j<homeworks.length;j++){
                        // console.log(classroomWorks[i].workId + "," + homeworks[j].id)
                        if(classroomWorks[i].workId==homeworks[j].id){
                            console.log("push ok")
                            option.tableData.push(homeworks[j]);
                        }
                    }  
                }
            }
        }
    },
    state:{
        course:{},
        classrooms:[]
    }
}
export default student_class_room;