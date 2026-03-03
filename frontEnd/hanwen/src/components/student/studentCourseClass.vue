<template>
    <div style="height:88vh;width:100%;">
      <div style="padding:10px;">
        <!-- header  -->
        <el-row>
          <el-col :span="8">
            <div>
              <el-button icon="el-icon-plus" @click="callJoinClassRoom">加入课堂</el-button>
            </div>
          </el-col>
          <el-col :span="8">
            <div>
              <el-input placeholder="搜索课程" v-model="filterkey" class="input-with-select" @change="1">
                <el-button slot="append" icon="el-icon-search" @click="filterClassRooms"></el-button>
              </el-input>
            </div>
          </el-col>
        </el-row>
        <el-divider></el-divider>
  
      <!-- content  -->
      <div style="overflow-y: scroll; height: 520px;user-select: none;">
        
        <div  style="display: grid;">
          <div style="display:block;">
            <div style="display: flex;align-items: center;">
             
              <span style="font-weight: bold;">班级课堂</span>&nbsp;&nbsp;
            </div>
          </div>
          <div>
            <div   style="float:left;">
              <!-- 班级卡片显示区域 -->
              <div class="course-class class-size" v-for="(classroom,classroom_index) in filter?filterResult:classrooms" :key="'classroom'+classroom_index" :style="'background-color:'+getColor(classroom_index)+';'" >
                <!-- <router-link to="/my_course/classroom" class="qx_a"> -->
                <router-link :to="{
                    path:'/my_course/classroom',
                    query:{
                        courseId:classroom.courseId,
                        classroomId:classroom.classroomId,
                        classroomName:encodeURIComponent(classroom.classroomName),
                    }
                  }"  class="qx_a">
                  <div  style="position: absolute;">
                    <div class="class-size" style="display: flex;justify-content: center;align-items: center;">
                      <div>
                        <span>{{ classroom.classroomName }}</span><br>
                        <span>{{ classroom.courseName }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="classroom_button" style="position: absolute;">
                    <div class="class-size" style="display: flex;justify-content: flex-end;align-items: flex-end;">
                      <div style="padding:5px;">
                        <el-button icon="el-icon-delete" :circle="true" type="danger" size="mini" @click.prevent="callQuitClassRoom(classroom)"></el-button>
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
import uuidv4 from '@/uuid';
import { mapActions, mapMutations, mapState } from 'vuex'
export default {
    name:"StudentCourseClass",
    data(){
        return {
          filterkey:"",
          filterResult:[],
          filter:false
        }
    },
    mounted(){
      // 已由父级组件myCourse来初始化
        // this.init({
        //     data:{userId:this.user.id},
        //     callback:{
        //         success:(msg)=>{this.$message.success(msg)},
        //         error:(msg)=>{this.$message.error(msg)}
        //     }
        // });
      setTimeout(()=>{
        this.$bus.$emit('slidemenu-index',this.$route.fullPath);
        this.$bus.$emit("change-index",2);
      },500)
    },
    computed:{
        ...mapState("user",["user"]),
        ...mapState("student_class_room",["classrooms"])
    },
    watch:{
        filterkey:{
            immediate:true,
            handler(newVal){
                if(newVal.length==0){
                this.filter=false
                this.filterResult=[];
                }
            }
        }
    },
    updated(){
        console.log("index-",this.$route.fullPath,2)
        this.$bus.$emit('slidemenu-index',this.$route.fullPath);
        this.$bus.$emit("change-index",2);
    },
    methods:{
      callJoinClassRoom(){
        this.$prompt('请输入课堂邀请码', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          //匹配数字、大小写字母、-
          inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+/,
          // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
          inputErrorMessage: '邀请码不正确'
        }).then(({ value }) => {
          this.joinClassRoom({
            data:{
              userId:this.user.id,
              classroomId:value,
              recordId:uuidv4(),
              joinDatetime:new Date()
            },
            callback:{
              success:(msg)=>{
                console.log(msg)
                this.$message.success(msg);
                this.init({
                  data:{userId:this.user.id},
                  callback:{
                      success:(msg)=>{
                        // console.log(msg)
                        // this.$message.success(msg)
                      },
                      error:(msg)=>{this.$message.error(msg)}
                  }
              });
            },
              error:(msg)=>{this.$message.error(msg);}
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });       
        });

      },
        getClassRoom(courseIndex,classroomIndex){
            // return this.myCreate[courseIndex].classRoomList[classroomIndex];
        },
        ...mapActions("student_class_room",["init","joinClassRoom","quit_classroom"]),
        // ...mapActions("course_class",["init","my_create_classroom","update_classroom","delete_course","delete_classroom"]),
        // ...mapMutations("course_class",["CREATE_CLASSROOM","DELETE_COURSE","UPDATE_CLASSROOM"]),
        filterClassRooms(){
            if(this.filterkey.length==0){
                return;
            }
            let arr = this.classrooms.filter(item=>{
                return item.courseName.includes(this.filterkey)
            });
            this.filterResult=[];
            this.filterResult.splice(0,this.courses.length, ...arr);
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
        callQuitClassRoom(classroom){
            let content =`此操作将退出班级[${classroom.classroomName}],学习记录将被清空！ 是否继续?`;
            const callfun = ()=>{
                this.quit_classroom({
                    data:{
                        recordId:classroom.recordId
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
</style>
