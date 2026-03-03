<template>
    <div style="height: 12vh;">
        <div class="header">
            <div class="h_flex">
                <img data-v-6fc64725="" src="@/assets/logo.png"
                    class="h_logo">
                <ul data-v-6fc64725="" class="h_menus">
                    <!-- <li>
                        <router-link to="/index" :class="index==0?'active':''">首页</router-link>
                    </li> -->
                    <li>
                        <router-link to="/my_teach/course_class" :class="index==1?'active':''" @click.native="handleSelect('/my_teach/course_class')">我教的课</router-link>
                    </li>
                    <li>
                        <router-link to="/my_course/stu_course_class" :class="index==2?'active':''" @click="index=2">我听的课</router-link>
                    </li>
                    <li>
                        <!-- <div style="display:inline-flex; display: flex;align-items: center;">
                            <el-input type="text" placeholder="搜索" size="small"></el-input>
                            <el-button  icon="el-icon-search" size="small"></el-button>
                        </div> -->
                        <router-link :to="{
                            path:user.role==0?'/my_course/noticelist':'/my_teach/noticelist'
                        }" :class="index==0?'active':''">公告</router-link>
                    </li>
                    <li @click="index=3" v-if="!isLogin">
                        <router-link to="/login" :class="index==3?'active':''">登录/注册 </router-link>
                    </li>
                    <li @click="index=4" v-if="isLogin" class="menu-item-last">
                        <img class="imgicon" src="@/assets/defaultUserIcon.png" >
                        <el-dropdown  @command="handleCommand">
                            <span class="el-dropdown-user" :class="index==4?'active':''">
                                {{user.name+(user.role==0?' [学生]':' [教师]')}}<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="logout" >退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                    <!-- <li @click="index=4">
                        <router-link to="/test"><el-button>测试</el-button></router-link>
                    </li> --> 
                    <!-- <li>
                        <el-button @click="testMethods()">测试</el-button>
                    </li> -->
                </ul>
            </div>
        </div>
        <!-- <div style="position:absolute;background-color: rgb(255, 255, 255);z-index:1;width:100%;margin:0;border:1px solid #fff;box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.4);"></div> -->
        <div style="position:absolute;background-color: rgb(255, 255, 255);z-index:1;width:100%;margin:0;border-top:1px solid #a3a3a3;"></div>

    </div>
</template>

<script>
import axios from 'axios';
import { mapMutations, mapState } from 'vuex';
export default {
    name: "NavBar",
    data(){
        return{
            index:1
        }
    },
    mounted(){
        console.info(this.$options.name +" 发起初始化user模块 ")
        this.INIT();
        this.$bus.$on("change-index",(index)=>{
            this.index = index;console.log(this.index)
        });
    },
    beforeDestroy(){
        this.$bus.$off("change-index");
    },
    computed:{
        ...mapState("user",["isLogin","user"])
    },
    methods:{
        ...mapMutations("user",["LOGOUT","INIT"]),
        handleCommand(command) {
            if(command=="logout"){
                this.LOGOUT();
                this.$router.push("/login");
            }
        },
        testMethods(){
            this.$message.success("test");
        },
        handleSelect(slideMenu){
            console.log(slideMenu);
            this.$bus.$emit("'slidemenu-index",slideMenu)
        }
    },
    beforeRouteEnter(to,from,next){
      console.log("---beforeRouteEnter",to,from);
      if(isAuth){
          next();
      }else{
          this.$message.error("请先登录");
      }
    },
}
</script>

<style>
.header {
    padding-left: 32px;
    padding-right: 32px;
}

.h_flex {
    height: 12vh;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    /* height: 78px; */
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
}

.h_logo {
    width: auto;
    height: 40px;
    margin-right: 32px;
}
.h_menus {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
}
.header .h_menus li {
    margin-right: 32px;
    -ms-flex-negative: 0;
    flex-shrink: 0;
}
li {
    list-style: none;
}
.h_menus li a{
    text-decoration: none;
    color: black;
    padding:5px;
}
.h_menus li a:hover{
    color: blue;
    cursor:pointer;
}
.active {
    color: blue !important;
}
.menu-item-last {
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}
/* icon */
.imgicon {
    /* 头像 */
    border: 1px solid gray;
    border-radius: 50%;
    width: 35px;
    height: 35px;
    object-fit: cover;
    margin-right: 10px;
  }
</style>