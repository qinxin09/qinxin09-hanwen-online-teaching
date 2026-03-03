<template>
    <div class="body_bg">
        <div id="regist">
            <span style="margin: 20px 0px; font-size: 20px; display: inline-flex;">注册</span>
            <!-- <div style="padding: 0 0 20px 10px">
                
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
            
            <!-- 注册 -->
            <div class="regist_div">
                
                <el-form 
                :model="regist" 
                :rules="rules" 
                status-icon
                ref="regist"
                >
                    <el-form-item label="角色" prop="role" :label-width="formLabelWidth" size="mini">
                        <div style="border:1px solid #DCDFE6;;border-radius:5px;width:100%;">
                            <el-dropdown  @command="handleCommand">
                                <span class="el-dropdown-link">
                                    {{role==0?'学生':'教师'}}<i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item :command="0" icon="el-icon-user">学生</el-dropdown-item>
                                    <el-dropdown-item :command="1" icon="el-icon-user-solid">教师</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </div>
                    </el-form-item>
                    <el-form-item label="姓名" prop="name" :label-width="formLabelWidth">
                        <el-input v-model.trim="regist.name"  autocomplete="on"></el-input>
                    </el-form-item>
                    
                    <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
                        <el-select v-model="regist.sex" placeholder="请选择性别">
                            <el-option label="男" :value="1"></el-option>
                            <el-option label="女" :value="0"></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="年龄" prop="age" :label-width="formLabelWidth">
                        <el-input type="number" v-model.number="regist.age"></el-input>
                    </el-form-item>

                    <el-form-item label="手机号" prop="phone" :label-width="formLabelWidth">
                        <el-input v-model.number="regist.phone"  autocomplete="on"></el-input>
                    </el-form-item>
                    <el-form-item label="密    码" prop="password" :label-width="formLabelWidth">
                        <el-input
                        v-model.trim="regist.password"
                        type="password"
                        autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="password2" :label-width="formLabelWidth">
                        <el-input
                        v-model.trim="regist.password2"
                        type="password"
                        autocomplete="off"
                        ></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer" style="width:100%;display:flex;justify-content: center;align-items:center;">
                    <el-button @click="resetForm('regist')">重 置</el-button>
                    <el-button type="primary" @click="Regist('regist')">确 定</el-button>
                </span>
            </div>
            <router-link to="/login" class="tips">点击这里去登录</router-link>
        </div>
    </div>
  
</template>

<script>
import { mapActions } from 'vuex';
export default {
    name: 'Regist',
    data(){
        var checkAge = (rule, value, callback) => {
            if (!value) {
                return callback(new Error("年龄不能为空"));
            }
            setTimeout(() => {
                if (!Number.isInteger(value)) {
                callback(new Error("请输入数字值"));
                } else {
                if (value < 18  || value>120) {
                    callback(new Error("年龄限制18岁 ~ 120岁"));
                } else {
                    callback();
                }
                }
            }, 1000);
        }

        var checkSex = (rule, value, callback) => {
            if (value !=1 && value != 0) {
                return callback(new Error("性别不能为空"));
            }
            callback();
        }

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
        var checkPassword2 =  (rule, value, callback) => {
            if(value != this.regist.password){
                callback(new Error("密码不一致！"));
            }
            callback();
        }
        var checkName = (rule, value, callback) => {
            if (!value) {
                return callback(new Error("姓名不能为空"));
            }
            callback();
        };
        var checkAge = (rule, value, callback) => {
            if (!value) {
                return callback(new Error("性别不能为空"));
            }
            callback();
        };

        return{
            role: 0,
            regist: {
                name:"",
                age:null,
                sex:null,
                phone: null,
                password: "",
                password2:""
            },
            rules:{
                name:[{ validator: checkName, trigger: "blur" }],
                age:[{ validator: checkAge, trigger: "blur" }],
                sex: [{ validator: checkSex, trigger: "blur" }],
                phone:[{ validator: checkPhone, trigger: "blur" }],
                password:[{ validator: checkPassword, trigger: "change" }],
                password2:[{ validator: checkPassword2, trigger: "blur" }],
            },
            formLabelWidth: "90px",
        }
    },
    methods: {
        ...mapActions("user",["user_regist"]),
        handleCommand(command) {
            this.role=command;
        },
        resetForm(formName){
            this.$refs[formName].resetFields();
        },
        Regist(formName) {
            console.log(formName,this.$refs[formName])
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // 注册
                    this.user_regist({
                        data:{
                            role:parseInt(this.role),
                            ...this.regist
                        },
                        callback:{
                            success:(msg)=>{
                                // 登录成功的回调
                                this.$refs[formName].resetFields();
                                this.$message.success(msg);
                                setTimeout(()=>{
                                    this.$router.push({
                                        path:'/login'
                                    })
                                },2000);
                            },
                            error:(msg)=>{
                                //登录失败的回调
                                this.$message.error(msg);
                            }
                        }
                    })                    
                    
                } else {
                console.log("error submit!!");
                return false;
                }
            });
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
    /* border:1px solid #0d9dff; */
    border-radius: 5px;
    padding: 10px;

  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .regist_div{
    /* background: white; */
    padding: 10px;
    /* border-radius: 5px; */
  }
  #regist {
    background: white;
    border-radius: 5px;
    padding:20px;
    padding-right:40px;
  }
  .tips{
    text-decoration: none;
    color: #515151;
  }
</style>