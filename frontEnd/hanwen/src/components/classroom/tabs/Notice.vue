<template>
  <div>
    <DialogCreateNotice  v-if="user.role==1" :key="courseId+classroomId"></DialogCreateNotice>
    <el-table :data="notices(user.role)" style="margin-top:10px;width: 100%;border:1px solid gray; overflow-y: auto;" height="64.5vh">
      <el-table-column type="expand">
        <template slot-scope="props">
          <!-- <div style="padding:10px;">
            <el-row type="flex" justify="space-around">
              <el-col :span="3"><span>标题</span></el-col>
              <el-col :span="10"><span>内容</span></el-col>
              <el-col :span="3"><span>创建时间</span></el-col>
              <el-col :span="3" v-if="user.role==1"><span>状态</span></el-col>
            </el-row>

            <el-row type="flex" justify="space-around">
              <el-col :span="3"><span>{{ props.row.title }}</span></el-col>
              <el-col :span="10"><span>{{ props.row.content }}</span></el-col>
              <el-col :span="3"><span>{{ new Date(props.row.createTime).toLocaleDateString() }}&nbsp;&nbsp;{{ new Date(props.row.createTime).toTimeString().substring(0,8) }}</span></el-col>
              <el-col :span="3" v-if="user.role==1"><span>{{ props.row.state==1?"已发布":"草稿" }}</span></el-col>
            </el-row>
          </div> -->

          <div style="padding:10px;">
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

      
      <el-table-column label="状态" prop="state" sortable v-if="user.role==1">
        <template slot-scope="scope">
          <span style="margin-left: 10px;font-weight:bold;" :style="scope.row.state==1?'color:red;':'color:gray;'">{{ scope.row.state==1?"已发送":"草稿" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400" v-if="user.role==1">
        <template slot-scope="scope">
          <!-- <el-button @click="handleClick(scope.row)" :type="scope.row.status==1?'danger':'primary'" size="small" >{{scope.row.status==1?'封禁':'解封'}}</el-button> -->
          <el-button-group>
            <el-button v-if="scope.row.state==0" type="primary" icon="el-icon-s-promotion" @click="publishNotice(scope.row)">发布</el-button>
            <el-button v-else type="warning" icon="el-icon-warning" @click="publishNotice(scope.row,true)">撤回</el-button>
            <el-button type="success" icon="el-icon-edit" @click="editNotice(scope.row)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="callDeleteNotice(scope.row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
import uuidv4 from '@/uuid';
import { mapActions, mapGetters, mapState } from 'vuex';
import DialogCreateNotice from "./DialogCreateNotice.vue"
export default {
    name:"Notice",
    components:{
      DialogCreateNotice
    },
    data(){
      return{
        courseId:"",
        classroomId:"",
        datasource:null
      }
    },
    computed:{
      ...mapGetters("notice",["notices"]),
      ...mapState("user",["user"])
    },
    methods:{
      ...mapActions("notice",["createNotice","init","updateNotice","deleteNotice"]),

      publishNotice(data,withdraw){
        //撤回操作
        if(withdraw){
          return this.callUpdateNotice({...data,state:0});
        }
        this.callUpdateNotice({...data,state:1});
      },
      editNotice(data){
        this.$bus.$emit("edit-notice", JSON.parse(JSON.stringify(data)));
        console.log(data);
      },
      callDeleteNotice(data){
        this.$confirm('此操作将永久删除该公告, 是否继续?', '提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteNotice({
            data:data,
            callback:{
              success:(msg)=>{
                this.$message.success(msg);
              },
              error:(msg)=>{
                this.$message.error(msg);
              }
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      },
      callUpdateNotice(notice){
        const option={
          data:{
            ...notice
          },
          callback:{
            success:(msg)=>{
              this.$message.success(msg);
              this.$bus.$emit("saved-notice-success");
            },
            error:(msg)=>{
              this.$message.error(msg);
            }
          }
        };
        this.updateNotice(option);
      }
    },
    mounted(){
      
      this.courseId=this.$route.query.courseId;
      this.classroomId=this.$route.query.classroomId;
      this.init({
        data:{
          courseId:this.courseId
        },
        callback:{
          success:(msg)=>{
            // this.$message.success(msg);

          },
          error:(msg)=>{
            this.$message.error(msg);
          }
        }
      })
      // this.$alert(this.courseId, "标题", {type:"success"})
      this.$bus.$on("create-notice",(notice)=>{
        console.log("收到create-notice,",notice);
        const option={
          data:{
            ...notice,
            courseId:this.courseId,
            createTime:new Date(),
            id:uuidv4(),
            state:0//草稿为0
          },
          callback:{
            success:(msg)=>{
              this.$message.success(msg);
              this.$bus.$emit("saved-notice-success");
            },
            error:(msg)=>{
              this.$message.error(msg);
            }
          }
        };
        this.createNotice(option);
      })
      this.$bus.$on("update-notice",(notice)=>{
        console.log("收到update-notice,",notice);
        this.callUpdateNotice(notice);
      })
    },
    beforeDestroy(){
      this.$bus.$off("create-notice");
      this.$bus.$off("update-notice");
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