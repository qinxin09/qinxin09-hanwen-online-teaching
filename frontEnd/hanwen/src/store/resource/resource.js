import {Interface} from "@/global";
import axios from 'axios'
const resource={
    namespaced:true,
    actions:{
        getDir(context,data){
            axios.post(Interface.resource.get_resource,data.data).then(
                res=>{
                    if(res.data.code==200){
                        data.callback.success(res.data.msg,res.data.data)
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
        }
    },
    mutations:{
        ADD_DIR(state,data){
            state.resourcesMap[data.id]=data.data
        },
        SET_FOLDERS(state,data){
            state.folders.splice(0,state.folders.length,data)
        },
        SET_FILES(state,data){
            state.files.splice(0,state.files.length,data)
        },
        
    },
    getters:{
        folders(state){
            return state.folders
        },
        files(state){
            return state.files
        },
        resourcesMap(state){
            return state.resourcesMap
        }
    },
    state:{
        folders:[],
        files:[],
        resourcesMap:{
            "/":[
                {
                    fileName:"",
                    type:"dir"
                }
            ]
        }
    }
}
export default resource;