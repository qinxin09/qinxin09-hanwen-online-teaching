import { Interface } from "@/global";
import axios from "axios";

const notice={
    namespaced:true,
    actions:{
        createNotice(context,data){
            console.log("===>",data);
            axios.post(Interface.teacher.create_notice,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("CREATE_NOTICE",data.data);
                    }else{
                        data.callback.error(res.data.msg);
                    }
                },
                error=>{
                    data.callback.error(error);
                }
            ).catch((e)=>{
                console.error("创建公告失败！",e);
            })
        },
        
        updateNotice(context,data){
            console.log("===>",data);
            axios.post(Interface.teacher.update_notice,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("UPDATE_NOTICE",JSON.parse(JSON.stringify(data.data)));
                    }else{
                        data.callback.error(res.data.msg);
                    }
                },
                error=>{
                    data.callback.error(error);
                }
            ).catch((e)=>{
                console.error("创建公告失败！",e);
            })
        },
        deleteNotice(context,data){
            axios.post(Interface.teacher.delete_notice,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        context.commit("DELETE_NOTICE",JSON.parse(JSON.stringify(data.data)));
                    }else{
                        data.callback.error(res.data.msg);
                    }
                },
                error=>{
                    data.callback.error(error);
                }
            ).catch((e)=>{
                console.error("删除公告失败！",e);
            })
        },
        init(context,data){
            //获取公告
            axios.post(Interface.teacher.get_notices,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        // 按时间先后排序，最新在前
                        res.data.data.sort((a,b)=>{return new Date(b.createTime) - new Date(a.createTime)});
                        context.commit("INIT_NOTICES",res.data.data);
                    }else{
                        context.commit("INIT_NOTICES",[]);
                    }
                },
                error=>{
                    console.error(error);
                }
            ).catch((e)=>{
                console.error("获取公告失败！",e);
            })
        },
        initCourseNotices(context,data){
            //获取公告
            axios.get(Interface.teacher.get_user_course_notices+"?userId="+data.data.userId).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                        // 按时间先后排序，最新在前
                        res.data.data.sort((a,b)=>{return new Date(b.createTime) - new Date(a.createTime)});
                        context.commit("INIT_COURSE_NOTICES",res.data.data);
                    }else{
                        context.commit("INIT_COURSE_NOTICES",[]);
                    }
                },
                error=>{
                    console.error(error);
                }
            ).catch((e)=>{
                console.error("获取公告失败！",e);
            })
        }
    },
    mutations:{
        CREATE_NOTICE(state,data){
            //插入头部
            state.notices.unshift(data);
            // state.notices.push(data);
        },
        UPDATE_NOTICE(state,data){
            for(let i=0;i<state.notices.length;i++){
            // console.log(state.notices[i].id,data.id,state.notices[i].id==data.id);
            if(state.notices[i].id==data.id){
                    state.notices.splice(i,1,data);
                    return;
                }
            }
            console.error("修改公告失败！");
        },
        DELETE_NOTICE(state,data){
            for(let i=0;i<state.notices.length;i++){
                if(state.notices[i].id==data.id){
                    state.notices.splice(i,1);
                    return;
                }
            }
            console.error("删除公告失败！");
        },
        INIT_NOTICES(state,data){
            state.notices.splice(0);
            state.notices.push(...data);
        },
        INIT_COURSE_NOTICES(state,data){
            state.coursesNotices.splice(0);
            state.coursesNotices.push(...data);
        }
    },
    getters:{
        notices(state){
            //role==1 ,return state.notices
            return (role)=>{
                if(role==1){
                    return state.notices;
                }else if(role==0){
                    return state.notices.filter((notice)=>{
                        return notice.state==1;
                    })
                }
            }
        }
    },
    state:{
        notices:[],
        coursesNotices:[]
    }
}
export default notice;