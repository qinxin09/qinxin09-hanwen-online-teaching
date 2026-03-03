<template>
    <div>
      <!-- div -->
      <el-dialog title="发布作业/考试" :visible="publishHomeworkDialogVisible" @open="printData()" @close="closeDialog" width="80%">
        <!-- 选择班级 -->
        <div style="padding:0 20px;" >
            <el-select v-model="classroomId" placeholder="请选择班级" @change="handleSelectChange">
                <el-option
                v-for="item in classrooms"
                :key="item.classroomId"
                :label="item.name"
                :value="item.classroomId">
                </el-option>
            </el-select>
        </div>
        <hr>
        列表：
        <div style="padding:0 20px;"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table
                :data="tableData"
                style="width: 100%"
                height="50vh"
                :default-sort = "{prop: 'date', order: 'descending'}"
                >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <!-- 显示作业内容 -->
                        <div style="margin:20px;overflow-y: auto;height:300px;">
                            <HomeworkTopics :homeworkTemp="JSON.parse(JSON.stringify(props.row))" :viewMode="true" />
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
                    prop="startDateTime"
                    label="时间"
                    sortable
                    width="320">
                    <template slot-scope="scope">
                        <!-- {{ new Date(props.row.startDate).toDateString() }} -->
                        <span style="margin-left: 10px">{{ new Date(scope.row.startDateTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(scope.row.startDateTime).toTimeString().substring(0,8) }} ~ {{ new Date(scope.row.endDateTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(scope.row.endDateTime).toTimeString().substring(0,8) }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="startDateTime"
                    label="操作"
                    sortable
                    width="400" >
                    <template slot-scope="scope">
                        <!-- {{ new Date(props.row.startDate).toDateString() }} -->
                        <!-- <span style="margin-left: 10px">{{ new Date(scope.row.startDateTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(scope.row.startDateTime).toTimeString().substring(0,8) }}</span> -->
                        <el-button-group>
                            <el-button v-if="!checkPublish(scope.row.id)" type="primary" icon="el-icon-s-promotion"  @click="callPublish(scope.row,false)">发布</el-button>
                            <el-button v-else type="warning" icon="el-icon-warning" @click="callPublish(scope.row,true)">撤回</el-button>
                            <el-button type="danger" icon="el-icon-delete" @click="callDelete(tableData,scope.$index)">删除</el-button>
                        </el-button-group>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        
      </el-dialog>
    </div>
  </template>
  
  <script>
import HomeworkTopics from '@/components/classroom/tabs/homework/homeworktopics.vue'
import { mapActions, mapGetters, mapState } from 'vuex';
import axios from 'axios';
import { Interface } from '@/global';
export default {
    name:"DialogPublishHomework",
    props:["publishHomeworkDialogVisible","closePublishHomeworkDialog","classrooms","homeworks"],
    components:{
        HomeworkTopics
    },
    data(){
        return {
            classroomId:"",
            classroomWorks:[],
            loading:false
        }
    },
    computed:{
        tableData(){
            if((this.homeworks+"").length>0){
                return JSON.parse(this.homeworks+"");
            }
            
            return [];
            
        }
    },
    mounted(){
        this.$bus.$on("publish-homework",()=>{
            this.getClassroomWork({
                data:{
                    classroomId:this.classroomId
                },
                callback:{
                    success:(msg)=>{
                        this.$message.success(msg);
                    },
                    error:(msg)=>{
                        this.$message.error(msg);
                    }
                }
            });
        })
    },
    beforeDestroy(){
        this.$bus.$off("publish-homework");
    },
    methods:{
        checkPublish(workId){
            for(let i=0;i<this.classroomWorks.length;i++){
                if(this.classroomWorks[i].workId==workId){
                   return true;
                }
            }
            return false;
        },
        handleSelectChange(value){
            //重新获取当前班级里的作业
            this.loading=true;
            axios.get(Interface.teacher.get_classroomworks+"?classroomId="+value).then(
                res=>{
                    this.loading=false;
                    this.classroomWorks.splice(0);
                    if(res.data.code==200){
                        this.classroomWorks.push(...res.data.data);
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
        ...mapActions("course_class",["teacher_saveHomework","teacher_clean_classroom_works","saveClassroomWork","revokeClassroomWork","getClassroomWork"]),
        
        closeDialog(){
            this.closePublishHomeworkDialog();
        },
        printData(){
            console.log(JSON.parse(this.homeworks))
        },
        callPublish(homework,isRevoke){
            if(this.classroomId == ""){
                return this.$alert("请先选择班级！");
            }
            if(!isRevoke){
                this.saveClassroomWork({
                    data:{
                        workId:homework.id,
                        classroomId:this.classroomId
                    },
                    callback:{
                        success:(msg)=>{
                            this.$message.success(msg);
                            this.classroomWorks.push({
                                classroomId:this.classroomId,
                                workId:homework.id,
                            })
                        },
                        error:(msg)=>{
                            this.$message.error(msg);
                        }
                    }
                })
            }else {
                this.revokeClassroomWork({
                    data:{
                        workId:homework.id,
                        classroomId:this.classroomId
                    },
                    callback:{
                        success:(msg)=>{
                            this.$message.success(msg);
                            this.classroomWorks.splice(this.classroomWorks.indexOf(this.classroomWorks.find(item=>item.workId == homework.id)),1)
                        },
                        error:(msg)=>{
                            this.$message.error(msg);
                        }
                    }
                })
            }
            
        },
        callDelete(tableData,index){
            let temp = JSON.parse(JSON.stringify(tableData));
            let delWork = temp.splice(index,1);
            this.$confirm('确认删除该作业['+delWork[0].title+']?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                console.log("deleteHomework");
                console.log(temp);
                this.teacher_clean_classroom_works({
                    data:{workId:delWork[0].id},
                    callback:{
                        success:(msg)=>{
                            console.log(msg);
                        },
                        error:(msg)=>{
                            console.error(msg);
                        }
                    }
                });
                this.teacher_saveHomework({
                    data:{
                        id:this.classrooms[0].courseId,
                        works:JSON.stringify(temp)
                    },
                    callback:{
                        success:(msg)=>{
                            this.$message.success(msg);
                            // this.tableData.splice(index,1);
                        },
                        error:(msg)=>{
                            this.$message.error(msg);
                        }
                    }
                });
                
            })
            .catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            })
        }
      }
  }
  </script>
  
  <style scoped>
  
  </style>