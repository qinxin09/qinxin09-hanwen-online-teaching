<template>
  <div>
    
        <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="barChart" style="width: 600px;height:400px;"></div>
    <pre style="display:none;">
          学习进度：
          章节 10%
          members:
          {{ members }}
          <hr>
          classroom_studyrecord
          {{ classroom_studyrecord }}
          <hr>
          course:
          {{ course }}
          <hr>
          {{ chapters }}
          <br>
         {{ memberVideoProgress }}
        </pre>
  </div>
</template>

<script>
import * as echarts from "@/echarts/echarts.min.js";
import "@/echarts/echarts.simple.min.js";
import { mapActions, mapGetters, mapState } from 'vuex';
export default {
    name:"LearningProgress",
    data() {
      return {
        memberVideoProgress:{
          userName:[],
          progress:[]
        }
      };
    },
    
    mounted() {
      this.initData();
      // this.createPicTab()
      this.getMembersVideoProcess();
      setTimeout(()=>{
      },3000);
  },
  watch:{
    chapters(){
      return this.getMembersVideoProcess();
    },
    classroom_studyrecord(){
      return this.getMembersVideoProcess();
    },
    course(){
      return this.getMembersVideoProcess();
    },
    members(){
      return this.getMembersVideoProcess();
    }
  },
  computed:{
    ...mapState("common_data",["members","classroom_studyrecord","course"]),
    ...mapGetters("common_data",["chapters","getUserNameById"])
  },
  beforeDestroy(){
    this.$off("tabs-change");
  },
  methods:{
    ...mapActions("common_data",["initMembers","initClassroomAllRecord","ininCourse"]),
    getMembersVideoProcess(){
      this.memberVideoProgress.userName.splice(0);
      this.memberVideoProgress.progress.splice(0);

      if(this.chapters===undefined || this.chapters==null){return;}
      //获取本课程所有章节数量
      const videosNum=function(chapters){
        let sum=0;
        let c = JSON.parse(chapters)
        for(let i=0;i<c.length;i++){
          let s =c[i].sections;
          for(let j=0;j<s.length;j++){
            sum=parseInt(sum+s[j].videos.length);
          }
        }
        return sum;
      }(this.chapters)
      //计算统计的数据
      this.classroom_studyrecord.forEach(record => {
        // 统计用户姓名
        let userName =this.getUserNameById(record.userId);
        let videos_done=[];
        this.memberVideoProgress.userName.push(userName);
        // 统计用户对应的课程视频进度
        // console.log(record,record.studyRecord)
        if(record.studyRecord!=undefined && record.studyRecord!=null && record.studyRecord!=""){
          videos_done=JSON.parse(record.studyRecord).videos_done;
          videos_done = videos_done || {};
          this.memberVideoProgress.progress.push(parseFloat(Object.keys(videos_done).length*100/videosNum));
        }else{
          this.memberVideoProgress.progress.push(0);
        }
        // console.log(
        //   {
        //     "userName":userName,
        //     "videos_done.length":videos_done.length,
        //     "videosNum":videosNum
        //   }
        // )
      });
      this.createPicTab("课程视频进度",this.memberVideoProgress.userName,this.memberVideoProgress.progress);
    },
    initData(){
      // 点击切换到本标签页时开始准备数据
      this.$bus.$on("tabs-change",(name)=>{
        if(name=="learning_progress"){
          console.log("点击了 learning_progress");
          // 获取班级成员
          this.initMembers({
            data:{
              classroomId:this.$route.query.classroomId
            },
            callback:{
              success:(msg)=>{
                // this.$message.success(msg);
              },
              error:(msg)=>{
                // this.$message.error(msg);
              }
            }
          });
          // 获取班级学习记录
          this.initClassroomAllRecord({
            data:{
              classroomId:this.$route.query.classroomId
            },
            callback:{
              success:()=>{},
              error:()=>{}
            }
          })
          // 获取当前班级对应的课程
          this.ininCourse({
            data:{
              courseId:this.$route.query.courseId
            },
            callback:{
              success:()=>{},
              error:()=>{}
            }
          })
        
        }
      })
    },
    
    // 创建图表
    createPicTab(title,x_data,y_data){
      // 初始化ECharts实例
      let myChart = echarts.init(document.getElementById("barChart"));

      // 配置项
      let option = {
        title: {
          text: title
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} %',
        },
        xAxis: {
          // data: ["视频学习进度", "作业完成进度","d","d","d","d","d"]
          data: x_data
        },
        yAxis: {},
        series: [
          {
            name: "进度",
            type: "bar",
            data: y_data
          }
        ]
      }
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
  }
}
</script>

<style scoped>
 .container{
  width:100px;
  height: 100px;
 }
</style>