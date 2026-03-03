import {Interface} from "@/global";
import axios from 'axios'
import Vue from "vue";
const members={
    namespaced:true,
    actions:{
        initMembers(context,data){
            // 获取课程成员
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
        }
    },
    mutations:{
        SET_MEMBERS(state,data){
            state.inited=true;
            state.members=data;
        },
        REST_MEMBERS(state){
            state.inited=false;
            state.members=[];
        }
    },
    state:{
        inited:false,
        members:[]
    }
}
export default members;