<template>
    <div>
        <el-table :data="coursesNotices" style="width: 100%;overflow-y: auto;" height="87vh">
           <el-table-column type="expand">
                <template slot-scope="props">
                <div style="padding:10px;">
                    <el-row type="flex" justify="space-around" class="my-el-row">
                      <el-col :span="2"><span>所属课程</span></el-col>
                      <el-col :span="20"><span>{{ props.row.courseName }}</span></el-col>
                    </el-row>
                    
                    <el-row type="flex" justify="space-around" class="my-el-row">
                      <el-col :span="2"><span>标题</span></el-col>
                      <el-col :span="20"><span>{{ props.row.title }}</span></el-col>
                    </el-row>
                    <el-row type="flex" justify="space-around" class="my-el-row">
                      <el-col :span="2"><span>内容</span></el-col>
                      <el-col :span="20"><span>{{ props.row.content }}</span></el-col>
                    </el-row>
                    <el-row type="flex" justify="space-around" class="my-el-row">
                      <el-col :span="2"><span>创建时间</span></el-col>
                      <el-col :span="20"><span>{{ new Date(props.row.createTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(props.row.createTime).toTimeString().substring(0,8) }}</span></el-col>
                    </el-row>
                </div>
                </template>
            </el-table-column>
            <el-table-column label="所属课程" prop="courseName">
            </el-table-column>
            
            <el-table-column label="标题" prop="title">
            </el-table-column>
            <el-table-column label="内容" prop="content" width="200">
                <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.content.substring(0,10) }}{{ scope.row.content.length>10?'...':'' }}</span>
                </template>
            </el-table-column>

            <el-table-column label="创建时间" prop="content" width="200">
                <template slot-scope="scope">
                <span style="margin-left: 10px">{{ new Date(scope.row.createTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(scope.row.createTime).toTimeString().substring(0,8) }}</span>
                </template>
            </el-table-column>

            </el-table>
    </div>
</template>

<script>
import axios from 'axios';
import { mapActions, mapState } from 'vuex';
import { Interface } from '@/global';
export default {
    name:"Teacher_NoticeList",
    
    data(){
      return{
        datasource:null,
        coursesNotices:[]
      }
    },
    computed:{
      ...mapState("user",["user"])
    },
    methods:{
      initCourseNotices(){
        axios.get(Interface.teacher.get_teacher_notices+"?teacherId="+this.user.id).then(
          res=>{
            if(res.data.code==200){
              this.coursesNotices=res.data.data || [];
            }
          },
          error=>{
            console.log("获取通知失败",error);
          }
        ).catch(e=>{
          console.log("获取通知失败",e);
        })
      }
    },
    mounted(){
      this.initCourseNotices();
    //   同步左侧菜单栏
      setTimeout(()=>{
        this.$bus.$emit('slidemenu-index',this.$route.fullPath);
      },500)
      //同步顶部导航栏
      this.$bus.$emit("change-index",0);
    },
    destroyed(){
      console.log("Notice destroyed");
    }
}
</script>

<style scoped>
.demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .my-el-row{
    border:1px solid gray;
    border-bottom: none;
    padding:10px;
  }
  span{
    font-weight: bold;
  }
  .my-el-row:last-child{
    border-bottom: 1px solid gray;
  }
</style>