import {Interface} from "@/global";
import axios from 'axios'
import Vue from "vue";
const course_class={
    namespaced:true,
    actions:{
        create_course(context,data){
            console.log(data)
            axios.post(Interface.teacher.create_course,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        context.commit("CREATE_COURSE",{...data.data});
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
        update_classroom(context,data){
            console.log(data)
            axios.post(Interface.teacher.update_classroom,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
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
        my_create_classroom(context,data){
            console.log(data)
            axios.post(Interface.teacher.my_create_classroom,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg,{...data.data});
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
        delete_classroom(context,data){
            console.log(data)
            axios.post(Interface.teacher.delete_classroom,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit('DELETE_CLASSROOM',data.data);
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
        delete_course(context,data){
            console.log(data)
            axios.post(Interface.teacher.delete_course,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
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
            console.log("初始化课程init");
            axios.post(Interface.teacher.my_create,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg)
                        for(let i=0;i<res.data.data.length;i++){
                            res.data.data[i].chapters=JSON.parse(res.data.data[i].chapters);
                            // console.log("这里转为对象，",res.data.data[i].chapters)
                        }
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
        getChapterById(context,options){
            const id=options.id;
            let state = context.state;
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==id){
                    return options.callback.success(state.myCreate[i]);
                }
            }
            console.log("getChapterById error");
            return options.callback.error("获取失败");
        },
        saveChapters(context,options){
            console.log("this is vuex save chapters ",options);
            axios.post(Interface.teacher.save_chapters,options.data).then(
                res=>{
                    if(res.data.code==200){
                        options.callback.success(res.data.msg);
                    }else{
                        options.callback.error(res.data.msg);
                    }
                },
                error=>{console.error("保存失败",error)}
            ).catch((e)=>{
                console.log("出错了",e);
            })
        },
        teacher_saveHomework(context,option){
            axios.post(Interface.teacher.save_homework,option.data).then(
                res=>{
                    if(res.data.code==200){
                        option.callback.success(res.data.msg);
                        context.commit("TEACHER_SAVE_HOMEWORK",JSON.parse(JSON.stringify(option.data)));
                    }else if(res.data.code==400){
                        option.callback.error(res.data.msg);
                    }
                },err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        // 删除作业后，清除相关记录
        teacher_clean_classroom_works(context,option){
            axios.get(Interface.teacher.clean_classroom_works+"?workId="+option.data.workId).then(
                res=>{
                    if(res.data.code==200){
                        option.callback.success(res.data.msg);
                    }else if(res.data.code==400){
                        option.callback.error(res.data.msg);
                    }
                },err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        saveClassroomWork(context,option){
            axios.post(Interface.teacher.save_classroom_work,option.data).then(
                res=>{
                    if(res.data.code==200){
                        option.callback.success(res.data.msg);
                    }else if(res.data.code==400){
                        option.callback.error(res.data.msg);
                    }
                },err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        },
        revokeClassroomWork(context,option){
            axios.post(Interface.teacher.revoke_classroom_work,option.data).then(
                res=>{
                    if(res.data.code==200){
                        option.callback.success(res.data.msg);
                    }else if(res.data.code==400){
                        option.callback.error(res.data.msg);
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
        CREATE_COURSE(state,value){
            console.log("插入课程",value)
            state.myCreate.push(value);
        },
        CREATE_CLASSROOM(state,option){
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==option.courseId){
                    console.log("插入班级",option);
                    state.myCreate[i].classRoomList.push(option.data);
                    return;
                }
            }
            console.error("插入班级失败");
        },
        DELETE_CLASSROOM(state,classroom){
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==classroom.courseId){
                    let rooms=state.myCreate[i].classRoomList;
                    for(let j=0;j<rooms.length;j++){
                        if(rooms[j].classroomId==classroom.classroomId){
                            state.myCreate[i].classRoomList.splice(j,1);
                            return console.log("删除班级成功");
                        }
                    }
                }
            }
            console.error("删除班级失败");
        },
        UPDATE_CLASSROOM(state,option){
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==option.courseId){
                    let rooms=state.myCreate[i].classRoomList;
                    for(let j=0;j<rooms.length;j++){
                        // console.log(option,rooms[j].classroomId,option.classroomId,rooms[j].classroomId==option.classroomId);
                        if(rooms[j].classroomId==option.classroomId){
                            console.log("更新班级",option);
                            state.myCreate[i].classRoomList.splice(j,1,option.classroomData);
                            return console.log("更新成功");
                        }
                    }
                }
            }
            return console.error("更新班级失败");

        },
        DELETE_COURSE(state,id){
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==id){
                    console.log("删除课程",state.myCreate[i]);
                    state.myCreate.splice(i,1);
                    break;
                }
            }
        },
        INIT_COURSE(state,value){
            state.myCreate.splice(0,state.myCreate.length,...value);
            console.log("初始化课程Mutations emit-->init-course-finish");
            Vue.prototype.$bus.$emit("init-course-finish");
        },
        TEACHER_SAVE_HOMEWORK(state,value){
            for(let i=0;i<state.myCreate.length;i++){
                if(state.myCreate[i].id==value.id){
                    state.myCreate[i].works="";
                    state.myCreate[i].works=value.works;
                    break;
                }
            }
        }

    },
    getters:{
        getHomework(state){
            return (option)=>{
                //拿到课程id
                let id=option.courseId;
                for(let i=0;i<state.myCreate.length;i++){
                    if(state.myCreate[i].id==id){
                        return option.homeworks = state.myCreate[i].works;
                    }
                }
                return option.homeworks = [];
            }
        },
        getclassroomHomeworks(state){
            return (option)=>{
                if(state.myCreate.length==0){
                    return [];
                }
                //根据课程id拿到所有的作业
                let homeworks=[];
                for(let i=0;i<state.myCreate.length;i++){
                    if(state.myCreate[i].id==option.courseId){
                        homeworks = JSON.parse(state.myCreate[i].works);
                        break;
                    }
                }
                // 没有作业
                if(homeworks.length==0){
                    return [];
                }
                
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
        myCreate:[]
    }
}
export default course_class;