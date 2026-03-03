<template>
    <div>
      <el-empty v-if="chapters.length==0" description="该课程还没有设置教学内容">
        <el-button v-if="user.role==1" type="primary" @click="toChapterManager">去添加课程视频</el-button>
      </el-empty>
      <div v-show="chapters.length>0">
        <ShowDrawer/>
        <el-row>
          <el-col :span="17">
            <div class="video-content">
              <div style="height:100%;width:100%;position:relative;">
                <video style="width:100%;height:100%;" ref="videoPlayer" src="" controls ></video>
              </div>
            </div>
          </el-col>
          <el-col :span="7">
            <div class="course-video-list">
              <ul style="margin:5px;padding:0;">
                <li v-for="(chapter,chapter_index) in chapters" :key="chapter.id">
                  <div style="" class="chapter-section chapteritem">
                    <el-row>
                      <el-col :span="5">第{{ (chapter_index+1) }}章</el-col>
                      <el-col :span="12">{{ chapter.title }}</el-col>
                    </el-row>
 </div>
                  <div :class="playIndex==(chapter_index+','+section_index)?'chapter-section sectionitem playing':'chapter-section sectionitem'" v-for="(section,section_index) in chapter.sections" :key="section.id"  :ref="chapter_index+','+section_index" :style="processStyleGet(chapter_index+','+section_index)">
                    <el-row @click.native="playCoursevideo(section,chapter_index+','+section_index)">
                      <el-col :span="6">
                        <i :class="getCheckIconClass(chapter_index,section_index)" class="el-icon-success"></i>&nbsp;&nbsp;&nbsp;{{ (chapter_index+1)+"." + (section_index+1) }}
                      </el-col>
                      <el-col :span="12">{{ section.title }}</el-col>
                      <el-col :span="6" v-show="section.documents.length">
                        <el-button icon="el-icon-paperclip" circle size="mini" @click.prevent="showResource(section)"></el-button>
                      </el-col>
                    </el-row>
                  </div>

                </li>
              </ul>
            </div>
          </el-col>
        </el-row>
      </div>
        
    </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import { Interface } from '@/global.js'
import axios from 'axios';
import ShowDrawer from '../viewsource/drawer.vue'
export default {
    name:"Chapter",//章节内容
    props:["panelIndex"],
    components:{
      ShowDrawer
    },
    
    data(){
      return {
        timer:{},
        studyRecord:{},
        recordId:'',
        courseId:0,
        processColor:"#98ceff",//进度背景颜色
        playerInfo:{
          player:null,
          currentTime:0,
          duration:1,
        },
        courseName:"",
        chapters:[],
        playIndex:"0,0",
        videoPath:""
      }
    },
    watch:{
      panelIndex(newValue){
        if(newValue!="0"){
          this.playerInfo.player.pause();
        }
      },
      chapters:{
        deep:true,
        immediate:true,
        handler(newValue){
          console.log(newValue.length)
        }
      }
    },
    mounted(){
      this.playerInfo.player=this.$refs["videoPlayer"];
      // 监听视频播放进度更新事件，调用updateProgress函数
      this.playerInfo.player.addEventListener("timeupdate", ()=>{
        this.playerInfo.currentTime=this.playerInfo.player.currentTime;
        this.playerInfo.duration=this.playerInfo.player.duration;
        if(typeof(this.playIndex)=="undefined"){
          this.playIndex="0,0";
        }
      });
      console.log("Chapter mounted");
      // console.log(this.$route);
      this.courseId=this.$route.query.courseId;
      //点进来的初始化
      this.initChapter();
      //学生端刷新的初始化
      this.$bus.$on("init-classroom-ok",()=>{
        console.log("刷新引起的初始化");
        this.initChapter();
      });
      // 教师刷新页面的初始化
      this.$bus.$on("init-course-finish",()=>{
        console.log("$on-->init-course-finish");
        this.initChapter();
      });
      

      this.timer=setInterval(()=>{
        if(this.user.role==1){
          return;//教师不需要记录
        }
        if(this.process>=90){
          if(this.playIndex.length>=2){
            let index=this.playIndex.split(",");
            
            if(this.checkVideoDone(parseInt(index[0]),parseInt(index[1]))){
              // 已经看完此小结的90%
            }else{
              //记录已经完成了视频任务
              console.log("记录已经完成了视频任务");
              // this.studyRecord.videos_done[this.chapterId]=true;
              this.$set(this.studyRecord.videos_done,this.playIndex,true);
              console.log(this.studyRecord.videos_done,this.playIndex);
              // this.studyRecord.videos_done[this.playIndex]=true;
              this.updateStudyRecord({
                data:{
                  recordId:this.recordId,
                  studyRecord:JSON.stringify(this.studyRecord)},
                callback:{
                  success:(msg)=>{
                    // this.$message.success({
                    //   message:msg,
                    //   duration:1000
                    // });
                    // this.studyRecord.videos_done.push(this.playIndex+"");
                    // 响应式是否还在
                  },
                  error:(msg)=>{this.$message.error(msg)}
                }
              });
            }
          }
        }
      },3000);
      
    },
    beforeDestroyed(){
      console.log("Chater destroyed");
      this.$bus.$off("init-course-finish");
      this.$bus.$off("init-course-finish")
    },
    computed:{
      ...mapState("user",["user"]),
      ...mapGetters("student_class_room",["getClassroomById"]),
      process(){
        return this.playerInfo.currentTime*100/this.playerInfo.duration;
      },
      chapters2(){
        return this.chapters;
      }
    },
    methods:{
      showResource(section){
        //显示课件
        this.$bus.$emit("show-attchement-files",section);
        setTimeout(()=>{
          this.playerInfo.player.pause();
        },300);
      },
      getCheckIconClass(chapter_index,section_index){
        if(this.user.role==1){
          return "check-icon none";
        }else if(this.user.role==0){
          if(this.studyRecord===null){
            console.error("studyRecord is null");
            return "check-icon none";
          }
          if(!this.studyRecord.videos_done){
            this.studyRecord.videos_done={};
            return "check-icon";
          }
          if(this.checkVideoDone(chapter_index,section_index)){
            return "check-icon done";
          }
          
          return "check-icon";
        }
      },
      // checkVideoDone(chapter_index,section_index){
      //   if(this.studyRecord==null){
      //     return false;
      //   }
      //   if(this.studyRecord.videos_done===undefined){
      //     this.studyRecord.videos_done=[];
      //     return false;
      //   }
      //   let result=false;
      //   for(let i=0;i<this.studyRecord.videos_done.length;i++){
      //     if(this.studyRecord.videos_done[i]==chapter_index+","+section_index){
      //       return result = true;
      //     }
      //   }
      //   return result;
      // },
      checkVideoDone(chapter_index,section_index){
          if(this.studyRecord.videos_done===undefined){
            this.studyRecord.videos_done={};
            return false;
          }else{
            return this.studyRecord.videos_done.hasOwnProperty(chapter_index+","+section_index);
          }
      },
      processStyleGet(index){
        if(index == this.playIndex){
          return 'background: linear-gradient(to right, '+this.processColor+', '+this.processColor+' '+this.process+'%, transparent '+this.process+'%); background-size: 100% 100%;';
        }
        return "";
      },
      async playCoursevideo(section,index){
        const videoplayer = this.playerInfo.player;
        videoplayer.pause();
        
        this.playerInfo.currentTime=0;
        if(section.videos.length==0){
          this.$message.error("视频不存在");
          // this.$refs["videoPlayer"].pause();
          this.playerInfo.player.pause();
          return;
        }
        let src=`${Interface.online_study.video_play}?teacherId=${section.videos[0].teacherId}&filePath=${section.videos[0].path+section.videos[0].name}`;
        // 视频是否存在
        try {
          const response = await axios.head(src);
          if (response.status === 200) {
            // console.log("Download link is valid");
          } else {
            return console.log("Download link is not valid");
          }
        } catch (error) {
          if(error.response.status==500){
            return this.$message.error("视频不存在！");
          }
          return console.error("An error occurred:", error);
        }

        if(typeof(section.videos[0])=='undefined'){
          return this.$message.error("视频不存在！");
        }
        // const videoplayer = this.$refs["videoPlayer"];
        if(this.playIndex==index){ 
          // 如果已暂停则继续播放
          console.log("videoplayer.paused",videoplayer.paused)
          if(videoplayer.paused){
            try{
              console.log("继续播放");
              videoplayer.play();
            }catch(e){
              console.log(e);
            }
          }else{
         // 如果正在播放则暂停
            console.log("暂停")
            videoplayer.pause();
          }

        }else{
          this.playIndex=index;
          // console.log(src);
          videoplayer.setAttribute("src",src);
          // 播放视频

        }
        // this.$message.info(section.videos[0].name.substring(36));
      },
      toChapterManager(){
        this.$router.push({
          // path:'/my_teach/chapter_mamage',
          name:"chapter_mamage",
          query:{
            id:this.$route.query.courseId
          }
        })
      },
      ...mapActions("course_class",["getChapterById"]),
      ...mapActions("student_class_room",["updateStudyRecord"]),
      getChapterByIdMethods(option){
        if(this.user.role==1){
          this.getChapterById(option);
        }else if(this.user.role==0){
          this.chapters.splice(0);
          let classroom=this.getClassroomById(this.courseId);
          if(classroom===undefined || classroom===null ){
            return console.log("这是学生端，刷新课程章节页面，需要由student-class-room init发起init-classroom-ok事件，本组件监听到再进行初始化");
          }
          console.log(classroom,this.courseId);
          let chapters_str = classroom.chapters;
          if(!chapters_str){
            return this.chapters.splice(0);
          }
          let chapters = JSON.parse(chapters_str);

          // console.log(chapters);
          
          // this.chapters.splice(0,this.chapters.length,...chapters);
          this.recordId=classroom.recordId;



          this.chapters.splice(0,this.chapters.length,...chapters);
          // console.log(classroom,classroom.studyRecord)
          if(classroom.studyRecord==""){
            console.log("没有学习记录");
            classroom.studyRecord="{}";
          }
          this.studyRecord=JSON.parse(classroom.studyRecord) || {};
          // this.$set(this._data,"chapters",chapters);console.log(this);
          // this.$set(this._data,"studyRecord",JSON.parse(classroom.studyRecord));
        }
        if(this.chapters.length==0){
          return console.log("没有章节");
        }
        this.playCoursevideo(this.chapters[0].sections[0]);
      },
      initChapter(){
        // this.course.id=this.$route.query.id;
        // console.log("route",this.$route);
        console.log("1 获取课程id",this.courseId);
        this.getChapterByIdMethods({id:this.courseId,
          callback:{
            success:(course)=>{
              let chapters=course.chapters;
              if(chapters==null){
                // 说明没有章节
                this.courseName=course.courseName;
                console.log("chapters is null");
                chapters=[];
              }
              this.$set(this._data,"chapters",chapters);
            },
            error:(err)=>{
              console.error(err);
              this.$set(this._data,"chapters",[]);
            }
          }
        })
      }
    }
    
}
</script>

<style>
.check-icon{
  color:#d7d7d7;
}
.check-icon.none{
  display: none;
}
.check-icon.done{
  color:green;
}
.video-content{
  border:1px solid black;
  height:70vh;
  width:100%;
  background-color:black;
}
.course-video-list {
  border:1px solid black;
  height:70vh;
  width:100%;
  overflow-y: auto;
}
.chapter-section {
  border:1px solid purple;
  margin:5px;
  padding:5px;
  cursor: pointer;
}
.chapter-section:hover{
  background-color: bisque;
}
.chapter-section.chapteritem{
  background-color: bisque;
}
.chapter-section.sectionitem{
  padding-left:10px;
}
.playing{
  color:blue;
}
</style>