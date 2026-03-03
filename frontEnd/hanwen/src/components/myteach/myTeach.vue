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
                        @select="handleSelect"
                        @close="handleClose"
                        background-color="#FFFFFFFF"
                        text-color="black"
                        active-text-color="blue">
                        
                        <!-- <el-submenu index="1">
                            <template slot="title">
                            <i class="el-icon-location"></i>
                            <span>课程班级</span>
                            </template>
                            <el-menu-item-group>
                            <template slot="title">分组一</template>
                            <el-menu-item index="1-1">选项1</el-menu-item>
                            <el-menu-item index="1-2">选项2</el-menu-item>
                            </el-menu-item-group>
                            <el-menu-item-group title="分组2">
                            <el-menu-item index="1-3">选项3</el-menu-item>
                            </el-menu-item-group>
                            <el-submenu index="1-4">
                            <template slot="title">选项4</template>
                            <el-menu-item index="1-4-1">选项1</el-menu-item>
                            </el-submenu>
                        </el-submenu> -->


                        <el-menu-item index="/my_teach/course_class">
                            <i class="el-icon-menu"></i>
                            <span slot="title">课程班级</span>
                        </el-menu-item>
                        <el-menu-item index="/my_teach/sources">
                            <i class="el-icon-folder"></i>
                            <span slot="title">资源库&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        </el-menu-item>
                        <el-menu-item index="/my_teach/noticelist">
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
    name:"MyTeach",
    data(){
        return {
            defaultActive: '/my_teach/course_class'
        }
    },
    computed:{
      ...mapState("user",["user"])
    },
    mounted(){
        console.log("我教的课，这里初始化我创建的课程数据 userid=",this.user.id);
        this.init({
            data:{id:this.user.id},
            callback:{
            success:(msg)=>{},
            error:(msg)=>{this.$message.error("获取我创建的课程失败！")}
            }
        });

        this.$bus.$emit('change-index',1);
        console.log(this.$route)
        // 获取default
        if(this.$route.fullPath == "/my_teach"){
            this.$router.push('/my_teach/course_class');
        }
        this.$bus.$on('slidemenu-index',(path)=>{
            console.log(path)
            this.defaultActive=path;
        });
    },
    beforeDestroy(){
        this.$bus.$off('slidemenu-index');
    },
    methods:{
        ...mapActions("course_class",["init"]),
        handleSelect(index, indexPath) {
            if(index == "/my_teach/course_class"){
                this.$bus.$emit("change-index",1)
            }
            console.log(index, indexPath);
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