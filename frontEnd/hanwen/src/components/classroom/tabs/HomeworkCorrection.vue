<template>
  <div>
    <el-row type="flex" v-if="user.role==1">
      <el-col>
        <el-radio-group v-model="tableDataType" @input="switchTableDate">
          <el-radio-button label="0">作业/考试</el-radio-button>
          <el-radio-button label="1">待批改</el-radio-button>
        </el-radio-group>
        <hr style="border: none; border-bottom: 1px solid #ebebeb;">
      </el-col>
      
    </el-row>
    <div style="padding:0 20px;"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table
                v-if="tableDataType=='0'"
                :data="tableData"
                style="width: 100%"
                :default-sort = "{prop: 'date', order: 'descending'}"
                >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <!-- 显示作业内容 -->
                        <!-- <div v-if="user.role==1 || props.row.type=='zuoye' " style="margin:20px;overflow-y: auto;height:300px;">
                            <HomeworkTopics :homeworkTemp="JSON.parse(JSON.stringify(props.row))" :viewMode="true" />
                        </div> -->
                        <!-- 不能显示考试内容 -->
                        <!-- <div v-else>
                          <el-result icon="error" title="错误提示" subTitle="不可查看考试内容！"></el-result>
                        </div> -->
                       <div style="padding:20px;">
                        <el-row type="flex">
                          <el-col :span="2">
                            <span style="font-weight:bold">标题</span>
                          </el-col>
                          <el-col :span="10">
                            {{ props.row.title }}
                          </el-col>
                        </el-row>
                        <hr>
                        <el-row type="flex">
                          <el-col :span="2">
                            <span style="font-weight:bold">总分</span>
                          </el-col>
                          <el-col :span="10">
                            {{ props.row.totalScore }}
                          </el-col>
                        </el-row>
                        <hr>
                        <el-row type="flex">
                          <el-col :span="2">
                            <span style="font-weight:bold">开放时间</span>
                          </el-col>
                          <el-col :span="10">
                            {{ new Date(props.row.startDateTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(props.row.startDateTime).toTimeString().substring(0,8) }}&nbsp;&nbsp;&nbsp;&nbsp;~&nbsp;&nbsp;&nbsp;&nbsp;
                            {{ new Date(props.row.endDateTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(props.row.endDateTime).toTimeString().substring(0,8) }}
                          </el-col>
                        </el-row>

                       </div>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="title"
                    label="标题"
                    sortable
                    width="220">
                </el-table-column>
                <el-table-column
                    label="类型"
                    width="220">
                    sortable
                    <template slot-scope="scope">
                      <span>{{ scope.row.type=="kaoshi"?"考试":"作业" }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="startDateTime"
                    label="状态"
                    sortable
                    width="220">
                    <template slot-scope="scope">
                        <!-- {{ new Date(props.row.startDate).toDateString() }} -->
                        <span style="margin-left: 10px">{{ checkState(scope.row) }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="user.role==0"
                    label="操作"
                    sortable
                    width="400" >
                    <template slot-scope="scope">
                        <!-- <el-button-group> -->
                            <DoWork :homeworkTemp="scope.row" :queryScore="queryScore"></DoWork>
                            <!-- <el-button size="mini">查询结果</el-button> -->
                        <!-- </el-button-group> -->
                    </template>
                </el-table-column>
            </el-table>



            <el-table
                v-if="tableDataType=='1'"
                :data="correctWorksTableData"
                style="width: 100%"
                >
                <el-table-column type="expand">
                  <template slot-scope="props">
                    <div style="padding:20px;">
                        <el-row type="flex">
                          <el-col :span="2">
                            <span style="font-weight:bold">用户名</span>
                          </el-col>
                          <el-col :span="10">
                            {{ props.row.userName }}
                          </el-col>
                        </el-row>
                      </div>
                  </template>
                </el-table-column>
                <el-table-column
                    label="用户名"
                    width="100">
                    <template slot-scope="scope">
                      <span>{{ scope.row.userName }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    label="作业/考试"
                    width="100">
                    sortable
                    <template slot-scope="scope">
                      <span>{{ scope.row.work.title }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    label="类型"
                    width="100">
                    sortable
                    <template slot-scope="scope">
                      <span>{{ scope.row.work.type=="kaoshi"?"考试":"作业" }}</span>
                    </template>
                </el-table-column>

                <el-table-column
                    label="成绩"
                    width="100"
                    sortable>
                    <template slot-scope="scope">
                      <span v-if="scope.row.work.state.state=='corrected'" :style="scope.row.work.state.totalMarks>60?'color:green':'color:red'">{{ scope.row.work.state.totalMarks }}</span>
                      <span v-else style="color:red;">未批改</span>
                      
                    </template>
                </el-table-column>

                <el-table-column
                    label="状态"
                    sortable
                    width="220">
                    <template slot-scope="scope">
                        <!-- {{ new Date(props.row.startDate).toDateString() }} -->
                        <span style="margin-left: 10px">{{ checkCollectState(scope.row) }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="user.role==1"
                    label="批改"
                    sortable
                    width="400" >
                    <template slot-scope="scope">
                        <el-button-group>
                            <CorrectWork :homeworkTemp="scope.row.work" :updateStudyData="updateStudyData" :studyDataTemp="scope.row.studyData"></CorrectWork>
                          </el-button-group>
                    </template>
                </el-table-column>
            </el-table>


        </div>
        
        
  </div>
</template>

<script>

import HomeworkTopics from '@/components/classroom/tabs/homework/homeworktopics.vue'
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import axios from 'axios';
import { Interface } from '@/global';
import DoWork from '@/components/classroom/DoWork.vue';
import CorrectWork from '@/components/classroom/CorrectWork.vue';
export default {
    name:"HomeworkCorrection",//作业管理
    components:{
        HomeworkTopics,DoWork,CorrectWork
    },
    data(){
        return {
            tableDataType:"0",
            classroomId:this.$route.query.classroomId,
            classroomWorks:[],
            tableData:[],
            loading:false,
            correctWorksTableData:[],
            studyRecords:{}
        }
    },
    computed:{
      ...mapState("user",["user"]),
      ...mapGetters("student_class_room",["getCourse","getHomeworks"]),
      teacher_getHomework:mapGetters("course_class",["getclassroomHomeworks"]).getclassroomHomeworks,
      ...mapState("members",["members"]),
    },
    mounted(){
      // 准备课程数据
      this.init();
      //加载班级里的作业
      this.$bus.$on("tabs-change",(name)=>{
        if(name=="homeworktab"){
          console.log("点击了homework")
          this.getClassroomWorks();

          this.REST_MEMBERS();
          this.initMembers({
            data:{
              classroomId:this.classroomId
            },
            callback:{
              success:(msg)=>{},
              error:(msg)=>{}
            }
          });

        }
      })
    },
    beforeDestroy(){
      this.$bus.$off("tabs-change");
    },
    methods:{
      queryScore(homework,studyRecord){
        // 1我的学习记录
        // 2解析works_done
        for(let i=0;i<studyRecord.works_done.length;i++){
          console.log("homeworkId="+homework.id,studyRecord.works_done[i].id);
          if(studyRecord.works_done[i].id==homework.id){
            let state = studyRecord.works_done[i].state;
            if(state.state!="corrected"){
              return this.$alert("还未批改", "提示",{type:"warning"})
            }
            const h = this.$createElement;
            this.$msgbox({
              title: '查询结果',
              message: h('p', null, [
                h('span', null, '成绩： '),
                h('i', { style: 'color: teal' }, state.totalMarks),
                h("br", null, ""),
                h('p', null, '评语： '+state.words),
              ]),
              showCancelButton: true,
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              beforeClose: (action, instance, done) => {
                done();
              }
            }).then(action => {});
            return console.log("state",state);
          }
        }
        
      },
      //更新本地的学习记录，并重新计算correctWorksTableData数据
      updateStudyData(studyData){
        // console.log("这是要更新的相关数据",studyData);
        // 找到对应的studyRecords
        // console.log(studyData);
        // console.log(this.studyRecords);
        //修改对应的学习记录
        for(let i=0;i<this.studyRecords.length;i++){
          if(studyData.recordId == this.studyRecords[i].recordId){
            this.studyRecords.splice(i,1,studyData);
            break;
          }
        }
        // 重新合并数据
        this.updateCorrectWorksTableData();
      },
      checkCollectState(data){
        if(data!=undefined){
          if(data.work.state!=undefined){
            return data.work.state.stateName;
          }
        }
        return "...";
      },
      ...mapActions("student_class_room",["ininCourse"]),
      ...mapActions("members",["initMembers"]),
      ...mapMutations("members",["REST_MEMBERS"]),
      init(){
        if(this.user.role==0){
          this.ininCourse({
            data:{
              courseId:this.$route.query.courseId
            },
            callback:{
              success:(msg)=>{
                console.log(msg);
              },
              error:(msg)=>{
                console.log(msg);
              }
            }
          });
        }else{
          // this.$alert("开发教师获取作业方法");
        }
      },
      checkState(homework){
        //开始
        if(new Date(homework.startDateTime).getTime()>=new Date().getTime()){
          return "未开始";
        }else if(new Date(homework.endDateTime).getTime()<=new Date().getTime()){
          return "已结束";
        }else{
          return "进行中";
        }
      },
      checkDone(homework){
        //
      },
      checkPublish(workId){
          for(let i=0;i<this.classroomWorks.length;i++){
              if(this.classroomWorks[i].workId==workId){
                  return true;
              }
          }
          return false;
      },
      getClassroomWorks(){
          //重新获取当前班级里的作业
          this.loading=true;
          axios.get(Interface.student.get_classroomworks+"?classroomId="+this.$route.query.classroomId).then(
              res=>{
                  this.loading=false;
                  this.classroomWorks.splice(0);
                  if(res.data.code==200){
                    // 获取相关id数据
                      this.classroomWorks.push(...res.data.data);
                      //根据id数据加载作业数据
                      // 学生
                      if(this.user.role==0){
                        this.getHomeworks({
                          classroomWorks:this.classroomWorks,
                          tableData:this.tableData
                        })
                        // 教师
                      }else if(this.user.role==1){
                        this.teacher_getHomework({
                          courseId:this.$route.query.courseId,
                          classroomWorks:this.classroomWorks,
                          tableData:this.tableData
                        })
                      }
                      
                  }else{
                      // console.error(res.data.msg);
                      // 没有发布作业
                  }
              },
              error=>{
                  this.loading=false;
                  console.error(error);
              }
          ).catch(e=>{
              this.loading=false;
              console.error(e);
          })

      },
      updateCorrectWorksTableData(){
        this.correctWorksTableData.splice(0);
        // 将成员数据和学习记录进行合并成新数据
        for(let i=0;i<this.studyRecords.length;i++){
          for(let j=0;j<this.members.length;j++){
            let works = this.getWorksData(this.studyRecords[i].studyRecord);
            for(let k=0;k<works.length;k++){
              if(this.studyRecords[i].userId == this.members[j].id){
                this.correctWorksTableData.push({
                  userName:this.members[j].name,
                  work:works[k],
                  studyData:this.studyRecords[i]
                })
              }
            }
            
          }
        }
      },
      // 
      switchTableDate(value){
        console.log(value,value==1,value=="1")
        if(value=="1" && this.user.role==1){
          if(this.correctWorksTableData.length==0){
            // 初始化学习记录数据
            axios.get(Interface.online_study.get_classroom_studyrecord+"?classroomId="+this.$route.query.classroomId).then(
              res=>{
                if(res.data.code==200){
                  this.studyRecords=res.data.data;
                  this.updateCorrectWorksTableData();
                }else{
                  this.correctWorksTableData.splice(0);
                }
              },
              error=>{
                console.error(error);
              }
            ).catch(e=>{
                console.error(e);
              }
            )
            
          }
        }
      },
      getWorksData(studyRecord){
        if((studyRecord+"").length>0){
          let obj = JSON.parse(studyRecord);
          if(obj!=null && obj !=undefined){
            if(obj.works_done!=null && obj.works_done !=undefined){
              return obj.works_done;
            }
          }
        }
        return [];
      }
    }
  }
  </script>

<style>

</style>