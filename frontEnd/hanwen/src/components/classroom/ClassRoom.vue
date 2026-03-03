<template>
  <div>
    <div style="padding:10px;">
      <el-page-header @back="goBack" :content="classroomName"></el-page-header>
    </div>
    <el-tabs type="border-card" @tab-click="tabClick" >
      <el-tab-pane label="课程章节">
        <Chapter :panelIndex="panelIndex"/>
      </el-tab-pane>
      <el-tab-pane name="members" label="成员列表">
        <Members/>
      </el-tab-pane>
      <el-tab-pane name="learning_progress" label="学习进度">
        <LearningProgress/>
      </el-tab-pane>
      <el-tab-pane name="homeworktab" label="班级作业/考试">
        <HomeworkCorrection/>
      </el-tab-pane>
      <el-tab-pane name="datastatistics" label="数据统计">
        <DataStatistics/>
      </el-tab-pane>
      <el-tab-pane label="公告">
        <Notice/>
      </el-tab-pane>
    </el-tabs>
    
    
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import Chapter from './tabs/Chapter.vue';
import DataStatistics from './tabs/DataStatistics.vue';
import HomeworkCorrection from './tabs/HomeworkCorrection.vue';
import LearningProgress from './tabs/LearningProgress.vue';
import Members from './tabs/Members.vue';
import Notice from './tabs/Notice.vue';


export default {
    name:"ClassRoom",
    components:{
      Chapter,DataStatistics,HomeworkCorrection,LearningProgress,Members,Notice
    },
    data(){
      return{
        panelIndex:0
        // "tab-position":"notices"
      }
    },
    computed:{
      // ...mapState("user",["isLogin","user"])
      classroomName(){
        return decodeURIComponent(this.$route.query.classroomName+"");
      }
    },
    methods:{
      ...mapActions("course_class",["getChapterById"]),
      ...mapActions("student_class_room",["student_getChapterById"]),
      // getChapter(){
      //   // (this.user.role==1?this.getChapterById:this.student_getChapterById)
      //   if(this.user.role==1){
      //     return this.getChapterById;
      //   }else if(this.user.role==0){
      //     return this.student_getChapterById;
      //   }
      // },
      goBack() {
        this.$router.go(-1);
      },
      tabClick(tab){
        this.panelIndex=tab.index;
        this.$bus.$emit("tabs-change",tab.name);
      }
    },
    mounted(){
      
    },
    
}
</script>

<style>
pre{
  border:1px solid #131212;
}
</style>