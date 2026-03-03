<template>
  <div style="height:88vh;width:100%;">
    <div style="padding:10px;">
      <!-- header  -->
      <el-row>
        <el-col :span="8">
          <div>
            <DialogCreateCourse/>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <el-input placeholder="搜索课程" v-model="filterkey" class="input-with-select" @change="filterCourses">
              <el-button slot="append" icon="el-icon-search" @click="filterCourses"></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <el-divider></el-divider>

    <!-- content  -->
    <div style="overflow-y: scroll; height: 520px;user-select: none;">
      <div v-for="(item,index) in filter?courses:myCreate" :key="'course'+index" style="display: grid;">
        <div style="display:block;">
          <div style="display: flex;align-items: center;">
            <!-- <h4>{{ item.courseName }}</h4>&nbsp;&nbsp;
            <el-tooltip class="item" effect="dark" content="新建班级" placement="bottom">
              <el-button icon="el-icon-plus" size="mini"></el-button>
            </el-tooltip> -->
            <span style="font-weight: bold;"  title="作业放到课程里面，在班级里面去选择要发布的作业及设置时间">{{ item.courseName }}</span>&nbsp;&nbsp;
            <!-- <span class="add-class">
              <el-tooltip class="item" effect="dark" content="新建班级" placement="bottom">
                <i class="el-icon-plus"></i>
              </el-tooltip> 
            </span> -->
            <DialogCreateClassRoom :course="item" :user="user" :create_classroom="my_create_classroom" :courseId="item.id" :mutation_CREATE_CLASSROOM="CREATE_CLASSROOM"/>
            
            <span class="add-class zjgl" style="width:20px;height:20px;" @click="toChapterManage(item)">
              <el-tooltip class="item" effect="dark" content="章节管理" placement="bottom">
                <svg t="1696493740317" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9828" width="20" height="20"><path d="M512 195.2L741.6 272 512 348.8 282.4 272 512 195.2m0-67.2L128 256v32l384 128 384-128v-32L512 128z m0 389.6l-320-104h-64v32l384 128 384-128v-32h-64l-320 104zM512 680L192 576h-64v32l384 128 384-128v-32h-64L512 680z m0 160L192 736h-64v32l384 128 384-128v-32h-64l-320 104z" p-id="9829" ></path></svg>
              </el-tooltip> 
            </span>

            <CreateHomeWorkDialog :course="item" :classrooms="item.classRoomList" />

            <!-- <span class="add-class zjgl" style="width:20px;height:20px;" @click="toChapterManage(item)">
              <el-tooltip class="item" effect="dark" content="作业管理" placement="bottom">
                <i class="el-icon-document-add"></i>
              </el-tooltip> 
            </span> -->

            <span class="add-class" @click="deleteCourse(item)">
              <el-tooltip class="item" effect="dark" content="删除课程" placement="bottom">
                <i class="el-icon-delete"></i>
              </el-tooltip> 
            </span>



          </div>
        </div>
        <div>
          <div   style="float:left;">
            <!-- 班级卡片显示区域 -->
            <div class="course-class class-size" v-for="(classroom,classroom_index) in item.classRoomList" :key="'classroom'+classroom_index" :style="'background-color:'+getColor(classroom_index)+';'" >
              <router-link :to="{
                path:'/my_teach/classroom',
                query:{
                    courseId:classroom.courseId,
                    classroomId:classroom.classroomId,
                    classroomName:encodeURIComponent(classroom.name),
                }
              }"  class="qx_a">
                <div  style="position: absolute;">
                  <div class="class-size" style="display: flex;justify-content: center;align-items: center;">
                    <span>{{ classroom.name }}</span>
                  </div>
                </div>
                <div class="classroom_button" style="position: absolute;">
                  <div class="class-size" style="display: flex;justify-content: flex-end;align-items: flex-end;">
                    <div style="padding:5px;">
                      <DialogUpdateClassRoom :getClassRoom="getClassRoom" :update_classroom="update_classroom" :mutation_UPDATE_CLASSROOM="UPDATE_CLASSROOM" :courseIndex="index" :roomIndex="classroom_index" />
                      <el-button icon="el-icon-delete" title="删除班级" :circle="true" type="danger" size="mini" @click.prevent="deleteClassRoom(classroom)"></el-button>
                      <el-button icon="el-icon-share" title="邀请码" :circle="true" type="primary" size="mini" @click.prevent="showClassRoomId(classroom)"></el-button>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
          </div>
        </div>

      </div>
    </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex'
import DialogCreateCourse from "./dialogCreateCourse.vue"
import DialogCreateClassRoom from "./dialogCreateClassRoom.vue"
import DialogUpdateClassRoom from "./dialogUpdateClassRoom.vue"
import CreateHomeWorkDialog from './dialogCreateHomework.vue'
export default {
    name:"CourseClass",
    data(){
      return {
        filterkey:"",
        courses:[],
        filter:false
      }
    },
    components:{
      DialogCreateCourse,
      DialogCreateClassRoom,
      DialogUpdateClassRoom,
      CreateHomeWorkDialog
    },
    
    computed:{
      ...mapState("course_class",["myCreate"]),
      ...mapState("user",["user"])
    },
    watch:{
      filterkey:{
        immediate:true,
        handler(newVal){
          if(newVal.length==0){
            this.filter=false
            this.courses=[];
          }
        }
      }
    },
    methods:{
      showClassRoomId(classroom){
        this.$confirm(classroom.classroomId, '课程邀请码', {
          confirmButtonText: '复制邀请码',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          let node = document.createElement("textarea");
          node.setAttribute("id", "copyString1234567890");
          node.value = classroom.classroomId;
          node.style.width = "0";
          node.style.height = "0";
          node.style.alpha = "0";
          document.body.parentNode.appendChild(node);
          node.select();
          if(document.execCommand("Copy")){
            this.$message.success("复制成功");
          }
          node.remove();

        }).catch(() => {
             
        });
      },
      getClassRoom(courseIndex,classroomIndex){
        return this.myCreate[courseIndex].classRoomList[classroomIndex];
      },
      ...mapActions("course_class",["my_create_classroom","update_classroom","delete_course","delete_classroom"]),
      ...mapMutations("course_class",["CREATE_CLASSROOM","DELETE_COURSE","UPDATE_CLASSROOM"]),
      filterCourses(){
        if(this.filterkey.length==0){
          return;
        }
        let arr = this.myCreate.filter(item=>{
          return item.courseName.includes(this.filterkey)
        });
        this.courses=[];
        this.courses.splice(0,this.courses.length, ...arr);
        this.filter=true;
        return this.courses;
      },
      getColor(index){
        // const color = ["#ff0000","#ff7f00","#ffff00","#00ff00"];
        // const color = ["#ff0000","#ff7f00","#ffff00","#00ff00"];
        // 6种浅色
        const color = ["rgb(100 185 255)","rgb(255 173 173)","rgb(255 194 106)","#ffff00","rgb(194 194 255)","rgb(167 250 167)"];
        return color[index%color.length];
      },
      deleteClassRoom(classroom){
        let content =`此操作将删除班级[${classroom.name}], 是否继续?`;
        const callfun = ()=>{
          this.delete_classroom({
            data:{
              classroomId:classroom.classroomId,
              courseId:classroom.courseId
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
        }
        this.deleteOption(content,callfun);
      },
      deleteOption(content,callfun){
        this.$confirm(content, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          callfun();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        });
      },
      deleteCourse(item){
        let content =`此操作将删除该课程[${item.courseName}]以及相关班级, 是否继续?`;
        const callfun = ()=>{
          this.delete_course({
            data:{
              id:item.id
            },
            callback:{
              success:(msg)=>{
                this.$message.success(msg);
                this.DELETE_COURSE(item.id);
              },
              error:(msg)=>{
                this.$message.error(msg);
              }
            }
          });
        }
        this.deleteOption(content,callfun);
      },
      toChapterManage(course){
        this.$router.push({
          // path:'/my_teach/chapter_mamage',
          name:"chapter_mamage",
          query:{
            id:course.id
          }
        })
      }
    }
}
</script>

<style scoped>
.course-class {
  border:1px solid #ccc;
  border-radius: 5px;
  float: left;
  margin: 10px;
  cursor: pointer;
  
  position: relative;
}

.class-size {
  width:300px;
  height:100px;
}
.add-class:hover {
  color:blue;
  cursor: pointer;
}
.add-class {
  font-size: 18px;
  padding: 10px;
}
.qx_a {
  text-decoration: none;
  color: black;
}
.classroom_button {
  display: none;
}
.qx_a:hover .classroom_button {
  display: block;
}
/* 章节管理 */
.zjgl svg path {
  fill:"black"
}
.zjgl:hover svg path {
  fill:blue;
}
</style>