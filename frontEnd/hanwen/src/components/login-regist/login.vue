<template>
    <div class="body_bg">
        <div id="login">
            <span style="margin: 20px 0px; font-size: 20px; display: inline-flex;">登录</span>
            <!-- <div style="padding: 40px;">
                <el-dropdown  @command="handleCommand">
                    <span class="el-dropdown-link">
                        当前角色：{{role==0?'学生':'教师'}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item :command="0" icon="el-icon-user">学生</el-dropdown-item>
                        <el-dropdown-item :command="1" icon="el-icon-user-solid">教师</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div> -->
            
            <!-- 学生登录 -->
            <div class="login_div">
                
                <el-form 
                :model="login" 
                :rules="rules" 
                status-icon
                ref="login"
                >
                    <el-form-item label="手机号" prop="phone" :label-width="formLabelWidth">
                        <el-input v-model.number="login.phone"  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="密    码" prop="password" :label-width="formLabelWidth">
                        <el-input
                        v-model.trim="login.password"
                        type="password"
                        autocomplete="off"
                        @keydown.enter.native="Login('login')"
                        ></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer" style="width:100%;display:flex;justify-content: center;align-items:center;">
                    <el-button @click="resetForm('login')">重 置</el-button>
                    <el-button type="primary" @click="Login('login')">确 定</el-button>
                </span>
            </div>
            <router-link to="/regist" class="tips">点击这里去注册</router-link>
        </div>
    </div>
  
</template>

<script>
import axios from 'axios';
import { mapActions } from 'vuex';
export default {
    name: 'Login',
    data(){
        var checkPhone = (rule, value, callback) => {
            if (!value) {
                return callback(new Error("号码不能为空"));
            }
            setTimeout(() => {
                if (!Number.isInteger(value)) {console.log(Number.isInteger(value),value)
                callback(new Error("请输入数字值"));
                } else {
                if (!(/^1[3-9]\d{9}$/.test(value))) {
                    callback(new Error("号码格式错误"));
                } else {
                    callback();
                }
                }
            }, 1000);
        };
        var checkPassword =  (rule, value, callback) => {
            if(value == ""){
                callback(new Error("密码不能为空！"));
            }
            callback();
        }

        return{
            role: 0,
            login: {
                phone: null,
                password: "",
            },
            rules:{
                phone:[{ validator: checkPhone, trigger: "blur" }],
                password:[{ validator: checkPassword, trigger: "blur" }],
            },
            formLabelWidth: "90px",
        }
    },
    methods: {
        ...mapActions("user",["user_login"]),
        handleCommand(command) {
            this.role=command;
        },
        Login(formName) {
            console.log(formName,this.$refs[formName])
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    const config={
                        data:{
                            role:parseInt(this.role),
                            ...this.login
                        },
                        callback:{
                            success:(msg,user)=>{
                                this.$message.success({
                                    message: msg,
                                    duration: 1000
                                });
                                this.$refs[formName].resetFields();
                                setTimeout(()=>{
                                    let page="";
                                    if(user.role==0){
                                        page = "/my_course/stu_course_class"
                                    }else if(user.role==1){
                                        page = "/my_teach/course_class"
                                    }
                                    this.$router.push({ path: "/" })
                                },1000);
                            },
                            error:(msg)=>{
                                this.$message.error({
                                    message: msg,
                                    duration: 1500
                                });
                            }
                        }
                    }
                    // 登录
                    this.user_login(config)
                    
                } else {
                console.log("error submit!!");
                return false;
                }
            });
        },
        resetForm(formName){
            this.$refs[formName].resetFields();
        }
    }
}
</script>

<style scoped>
.body_bg{
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto;
    background: url(@/assets/bg.jpg) no-repeat;
    background-position: 50%;
    background-size: cover;
}

.el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
    border:1px solid #0d9dff;
    border-radius: 5px;
    padding: 10px;

  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .login_div{
    /* background: white; */
    padding: 10px;
    /* border-radius: 5px; */
  }
  #login {
    background: white;
    border-radius: 5px;
    padding:20px;
  }
  .tips{
    text-decoration: none;
    color: #515151;
  }
</style>