<template>
    <div>
      <el-button :type="theWorkIsCorrected?'warning':'primary'" @click="todoCorrect()">{{ theWorkIsCorrected?"再次批改":"去批改"}}</el-button>
      <el-drawer
      title="内容"
      :visible.sync="showCorrectWorkDrawer"
      size="100%"
      @open="openCorrectWorkDrawer"
      :with-header="false">
          <!-- 显示作业内容 -->
          <div>
                <el-row type="flex" style="text-align: center;align-items: center;margin-top: 10px;">
                    <el-col :span="6">
                        <div style="">
                            <span>{{ homeworkTemp.title }}{{"总分："+ homeworkTemp.totalScore }}</span>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div style="font-size:18px;">
                            <span>当前得分：</span>
                            <span :style="totalScore<60?'color:red':'color:green'">{{totalScore }}</span>
                        </div>
                  </el-col>
                  <el-col :span="6">
                      <span>批改</span>
                      
                  </el-col>
                  <el-col :span="6">
                      <el-button type="success" @click="sendAnswer()">提交</el-button>
                      <el-button type="danger" @click="showCorrectWorkDrawer=false">关闭</el-button>
                  </el-col>
              </el-row>
          </div>
          <hr>
          <div style="overflow-y: auto;">
              <div style="margin:20px 70px;height:83vh;">
                  <HomeworkTopics :homeworkTemp="homeworkTemp" :viewMode="true" :correctMode="true" :notifyCurrentScore="getTotalScore" />
              </div>
          </div>
      </el-drawer>

      
    </div>
  </template>
  
  <script>
  import HomeworkTopics from '@/components/classroom/tabs/homework/homeworktopics.vue'
  import { mapActions, mapGetters, mapState } from 'vuex';
  export default {
      name:"CorrectWork",//作业管理
      props:["homeworkTemp","studyDataTemp","updateStudyData"],
      components:{
          HomeworkTopics
      },
      data(){
          return {
              showCorrectWorkDrawer:false,
              timer:null,
              studyRecord:{
                  works_done:[]
              },
              recordId:"",
              totalScore:0,
          }
      },
      mounted(){
          this.initstudyRecord();
          this.$bus.$on("tabs-change",(name)=>{
              if(name=="homeworktab"){
              console.log("点击了homework,=============")
              this.initstudyRecord();
              }
          })
      },
      beforeDestroy(){
        this.$bus.$off("tabs-change");
      },
      computed:{
          ...mapState("user",["user"]),
        //   ...mapState("student_class_room",["classrooms"]),
          ...mapGetters("student_class_room",["getClassroomById"]),
          theWorkIsCorrected(){
              return this.homeworkTemp.state.state=="corrected";
          }
      },
      methods:{
        getTotalScore(number){
            this.totalScore=number;
        },
        // 检查是否已批改
        // 拿到作业
        //   checkCorrectDone(){
        //       let homework=this.homeworkTemp;
        //       if(this.studyRecord.works_done.length>0){
        //           for(let i=0;i<this.studyRecord.works_done.length;i++){
        //               if(this.studyRecord.works_done[i].id==homework.id){
        //                 //   检查状态
        //                 if(this.studyRecord.works_done[i].state){
        //                     if(this.studyRecord.works_done[i].state.corrected){
        //                         return true;
        //                     }
        //                 }
        //               }
        //           }
        //       }
        //       return false;
        //   },
          initstudyRecord(){
              this.recordId = this.studyDataTemp.recordId;
              let studyRecord = this.studyDataTemp.studyRecord;
              //解析学习记录
              if(studyRecord.length>0){
                  this.studyRecord = JSON.parse(studyRecord);
              }else{
                  this.studyRecord={
                      works_done:[]
                  };
              }
              if(this.studyRecord.works_done===undefined || this.studyRecord.works_done.length===null){
                  this.studyRecord.works_done=[];
              }
            //   console.log("提交前的数据",this.studyRecord);
          },
          ...mapActions("student_class_room",["updateStudyRecord"]),
          openCorrectWorkDrawer(){
              console.log("打开了抽屉，批改作业")
          },
  
          sendAnswer(){
              let callback = (homework)=>{
                  console.log("这是批改的作业");
                  console.log(homework);
                  //更新学习记录
                  for(let i=0;i<this.studyRecord.works_done.length;i++){
                      if(this.studyRecord.works_done[i].id===homework.id){
                          this.studyRecord.works_done.splice(i,1,homework);
                          break;
                      }
                  }
                    //   return console.log("更新后的数据：",{
                    //     recordId:this.recordId,
                    //       studyRecord:JSON.stringify(this.studyRecord)
                    //   })
                  //更新父组件table行数据
                  this.updateStudyRecord({
                        data:{
                            recordId:this.recordId,
                            studyRecord:JSON.stringify(this.studyRecord)},
                            callback:{
                                success:(msg)=>{
                                        this.updateStudyData({
                                            ...this.studyDataTemp,
                                            studyRecord:JSON.stringify(this.studyRecord)
                                        });
                                        this.showCorrectWorkDrawer=false;
                                        this.$message.success({
                                            message:msg,
                                            duration:1000
                                        });
                                        this.$forceUpdate();
                                },
                            error:(msg)=>{this.$message.error(msg)}
                        }
                  });
              }
              this.$bus.$emit("online-send-answer",callback);//
          },
          todoCorrect(){
            this.showCorrectWorkDrawer=true;
          }
      }
  }
  </script>
  
  <style>
  
  </style>