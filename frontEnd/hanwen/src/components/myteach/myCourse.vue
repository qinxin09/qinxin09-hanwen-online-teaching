<template>
    <div class="container">
        <el-row class="tac">
            <el-col :span="3">
                <div style="height: 88vh;background-color: #FFF;border-right:1px solid gray;">
                    <!-- 开启路由导航，index作为路径 -->
                    <el-menu
                        router
                        :default-active="defaultActive"
                        class="el-menu-vertical-demo"
                        @open="handleOpen"
                        @close="handleClose"
                        background-color="#FFFFFFFF"
                        text-color="black"
                        active-text-color="blue">
                        
                        <el-menu-item index="/my_course/stu_course_class">
                            <i class="el-icon-menu"></i>
                            <span slot="title">课程班级</span>
                        </el-menu-item>
                        <el-menu-item index="/my_course/noticelist">
                            <i class="el-icon-message-solid"></i>
                            <span slot="title">公告&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        </el-menu-item>
                
                </el-menu>
                </div>
            </el-col>
            <el-col :span="21">
                <div class="myteach-content">
                    <router-view></router-view>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
export default {
    name:"MyCourse",
    data(){
        return {
            defaultActive: '/my_course/stu_course_class'
        }
    },
    computed:{
        ...mapState("user",["user"]),
    },
    mounted(){
        console.log("我听的课")
        this.$bus.$emit('change-index',2);
        // 获取default
        if(this.$route.fullPath == "/my_course"){
            this.$router.push('/my_course/stu_course_class');
        }

        // 初始化课程信息
        console.log("myCourse===>初始化课程信息");
        this.init({
            data:{userId:this.user.id},
            callback:{
                success:(msg)=>{
                    // this.$message.success(msg)
                },
                error:(msg)=>{this.$message.error(msg)}
            }
        });
        this.$bus.$on('slidemenu-index',(path)=>{
            this.defaultActive=path;
        });
    },
    beforeDestroy(){
        this.$bus.$off('slidemenu-index');
    },
    methods:{
        ...mapActions("student_class_room",["init","joinClassRoom","quit_classroom"]),
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        }
    }
}
</script>

<style scoped>
.container {
    height:85vh;
    text-align: left;
}
.el-menu-vertical-demo {
    border:none;
}
</style>
