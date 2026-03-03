<template>
  <div style="height:71vh;overflow-y: auto;">
      <div class="member title">
        <el-row>
          <el-col :span="2">&nbsp;</el-col>
          <el-col :span="4">用户名</el-col>
          <el-col :span="2">性别</el-col>
          <el-col :span="6">电话</el-col>
        </el-row>
      </div>

      <div class="member list" v-for="(member) in members" :key="member.id">
        <el-row>
          <el-col :span="2"><img class="imgicon" src="@/assets/defaultUserIcon.png" ></el-col>
          <el-col :span="4" :title="member.name">{{ member.name.length>15?member.name.substring(0,15)+'...':member.name }}{{ member.id==user.id?'(我)':'' }}</el-col>
          <el-col :span="2">{{ member.sex==1?'男':'女' }}</el-col>
          <el-col :span="6">{{ member.phone }}</el-col>
        </el-row>
      </div>

  </div>
</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex';
export default {
    name:"MemberList",
    data(){
      return{
        classroomId:this.$route.query.classroomId
      }
    },
    mounted(){
      this.$bus.$on("tabs-change",(name)=>{
        if(name=="members"){
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
      });
      
    },
    beforeDestroy(){
      this.$bus.$off("tabs-change")
    },
    computed:{
      ...mapState("members",["members"]),
      ...mapState("user",["user"]),
    },
    methods:{
      ...mapActions("members",["initMembers"]),
      ...mapMutations("members",["REST_MEMBERS"]),
    }
}
</script>

<style scoped>
.member{
  /* margin:5px; */
  padding:10px;
  border:1px solid gray;
  border-bottom: none;
  /* border-radius: 5px; */
}
.member:last-child{
  border-bottom:1px solid gray;
}
.imgicon {
    /* 头像 */
    border: 1px solid gray;
    border-radius: 50%;
    width: 35px;
    height: 35px;
    object-fit: cover;
    margin-right: 10px;
  }
  .el-row{
    box-sizing: border-box;
    align-items: center;
    justify-content: start;
    display: flex;
  }
</style>