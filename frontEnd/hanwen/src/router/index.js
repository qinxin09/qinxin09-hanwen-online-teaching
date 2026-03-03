//该文件专门用于创建整个应用的路由器
//vue2 安装 npm i vue-router@3
import VueRouter from 'vue-router';

import Vue from 'vue';
import Home from '@/components/home.vue';
import Index from '@/components/index/index.vue';
import Login from '@/components/login-regist/login.vue';
import Regist from '@/components/login-regist/regist.vue';
import MyTeach from '@/components/myteach/myTeach.vue';
import CourseClass from '@/components/myteach/teach/courseClass.vue';
import Resources from '@/components/myteach/resource/sources.vue';
import ClassRoom from '@/components/classroom/ClassRoom.vue';
import ChapterManager from '@/components/myteach/teach/ChapterManager.vue';


import MyCourse from '@/components/myteach/myCourse.vue';
import StudentCourseClass from '@/components/student/studentCourseClass.vue';
// import TestHome from '@/test/TestHome.vue';
import NoticeList from '@/components/student/notice/noticeList.vue';
import Teacher_NoticeList from '@/components/myteach/notice/noticeList.vue';
//创建一个路由器
const router = new VueRouter({
    routes:[
        {
            path:'/',
            component:Home,
            children:[
                // {
                //     path:"test",
                //     component:TestHome
                // },
                // {
                //     path:"index",
                //     component:Index
                // },
                {
                    path:"my_course",
                    component:MyCourse,
                    meta:{title:"我听的课",isAuth:true,role:0,tips:"请先用学生身份登录！"}, //需要权限验证 登录
                    children:[
                        {
                            path:"stu_course_class",
                            component:StudentCourseClass,
                            meta:{title:"课程班级",isAuth:true,role:0,tips:"请用学生身份登录！"},//需要权限验证 登录
                        },
                        {
                            path:"classroom",
                            component:ClassRoom,
                            meta:{title:"课堂",isAuth:true,role:0,tips:"请用学生身份登录！"},//需要权限验证 登录
                        },
                        {
                            path:"noticelist",
                            component:NoticeList,
                            meta:{title:"公告列表",isAuth:true,role:0,tips:"请用学生身份登录！"},//需要权限验证 登录
                        },
                    ]
                },
                {
                    path:"my_teach",
                    component:MyTeach,
                    meta:{title:"我教的课",isAuth:true,role:1,tips:"请用教师身份登录！"},//需要权限验证 登录
                    children:[
                        {
                            path:'course_class',
                            component:CourseClass,
                            meta:{title:"课程班级",isAuth:true,role:1,tips:"请用教师身份登录！"},//需要权限验证 登录
                        },
                        {
                            path:'sources',
                            component:Resources
                        },
                        {
                            path:"classroom",
                            component:ClassRoom
                        },
                        {
                            name:"chapter_mamage",
                            path:"chapter_mamage",
                            component:ChapterManager,

                            // path:"chapter_mamage",
                            // props({query:{id}}){
                            //     return {id}
                            // }
                        },{
                            path:"noticelist",
                            component:Teacher_NoticeList,
                            meta:{title:"公告列表",isAuth:true,role:1,tips:"请用教师身份登录！"},//需要权限验证 登录
                        }
                    ]
                }
            ]
        },
        {
            path:'/login',
            component:Login
        },
        {
            path:'/regist',
            component:Regist
        }
    ]
});

// 全局前置路由首位--初始化及每次路由切换之前被调用
router.beforeEach((to,from,next)=>{
    console.log(from.fullPath+"====>"+to.fullPath)
    function showMessage(){
        Vue.prototype.$confirm(to.meta.tips, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
            }).then(() => {
                return;
            }).catch((e) => {
                console.log("error",e)
                // Vue.prototype.$message({
                //     type: 'info',
                //     message: '已取消'
                // });          
        });
    }
    if(to.meta.isAuth){
        // console.log("需要权限验证",to.meta.title);
        let user = localStorage.getItem("user");
        if(user!=null && user !=""){
            console.log(JSON.parse(localStorage.getItem("user")).role,to.meta.role,to.path)
            if(JSON.parse(localStorage.getItem("user")).role == to.meta.role){
                next();
            }else{
                return showMessage();
            }
        }else{
            return showMessage();
        }
    }else{
        next();
    }
    
})
// 暴露路由器
export default router;

