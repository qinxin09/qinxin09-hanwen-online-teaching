import {Interface} from "@/global";
import axios from 'axios'
import Vue from "vue";
const user={
    namespaced:true,
    actions:{
        user_login(context,data){
            console.log(data)
            axios.post(Interface.user.user_login+"?role="+data.data.role,data.data).then(
                res=>{
                    if(res.data.code==200){
                        context.commit("SET_ISLOGIN",true);
                        data.callback.success(res.data.msg,res.data.data);
                        context.commit("SET_USER",res.data.data);
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
        user_regist(context,data){
            console.log(data.data)
            axios.post(Interface.user.user_regist+"?role="+data.data.role,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg);
                    }else if(res.data.code==400){
                        data.callback.error(res.data.msg);
                    }
                    console.log("res",res);
                },
                err=>{
                    console.error(err)
                }
            ).catch((e)=>{
                console.error(e);
            })
        }
    },
    mutations:{
        SET_ISLOGIN(state,value){
            console.log("state,value",state,value);
            state.isLogin=value;
        },
        SET_USER(state,value){
            localStorage.setItem("user",JSON.stringify(value));
            state.user=value;
        },
        LOGOUT(state,value){
            localStorage.removeItem("user");
            state.isLogin=false;
            state.user={};
        },
        INIT(state,value){
            let user=localStorage.getItem("user");
            if(user!=null && user!=null){
                state.isLogin=true;
                state.user=JSON.parse(user);
            }
            console.log("初始化用户信息 init-user ok");
            Vue.prototype.$bus.$emit("init-user",state.user);
        }
    },
    state:{
        isLogin:false,
        user:{}
    }
}
export default user;