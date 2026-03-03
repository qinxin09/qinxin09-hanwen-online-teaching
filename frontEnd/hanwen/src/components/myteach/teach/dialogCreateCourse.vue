<template>
  <div>
    <el-button :plain="true" @click="openDialog" icon="el-icon-plus">新建课程</el-button>
    <el-dialog title="创建课程" :visible.sync="dialogFormVisible">
        <div style="padding:0 20px;">
            <el-form :model="form" ref="ruleForm" label-position="left" size="medium" :rules="rules">
                <el-form-item label="课程名称" prop="courseName" :label-width="formLabelWidth">
                    <el-input v-model="form.courseName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="课程类型" prop="courseType" :label-width="formLabelWidth">
                    <el-select v-model="form.courseType" placeholder="请选择类型">
                        <el-option label="小学" value="xiaoxue"></el-option>
                        <el-option label="初中" value="chuzhong"></el-option>
                        <el-option label="高中" value="gaozhong"></el-option>
                        <el-option label="大学" value="daxue"></el-option>
                        <el-option label="其它" value="qita"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选择标签" prop="courseTag" :label-width="formLabelWidth">
                    <el-select v-model="form.courseTag" placeholder="请选择标签">
                        <el-option label="语文" value="yuwen"></el-option>
                        <el-option label="数学" value="shuxue"></el-option>
                        <el-option label="英语" value="yingyu"></el-option>
                        <el-option label="物理" value="wuli"></el-option>
                        <el-option label="化学" value="huaxue"></el-option>
                        <el-option label="生物" value="shengwu"></el-option>
                        <el-option label="地理" value="dili"></el-option>
                        <el-option label="政治" value="zhengzhi"></el-option>
                        <el-option label="历史" value="lishi"></el-option>
                        <el-option label="其它" value="qita"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  label="课程介绍" prop="courseDesc" :label-width="formLabelWidth">
                    <el-input type="textarea" :rows="2" placeholder="请输入课程介绍" v-model="form.courseDesc"> </el-input>
                </el-form-item>
                <el-form-item label="课程时间" required>
                    <el-row :gutter="4">
                        <el-col :span="8">
                        <el-form-item prop="startDate">
                            <el-date-picker type="date" placeholder="开始日期" v-model="form.startDate" style="width: 100%;"></el-date-picker>
                        </el-form-item>
                        </el-col>
                        <el-col class="line" :span="2" >
                            <div style="display:flex;justify-content: center;"><span>-</span></div>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item prop="endDate">
                                <el-date-picker type="date" placeholder="结束日期" v-model="form.endDate" style="width: 100%;"></el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </div>
        </el-dialog>
  </div>
</template>

<script>
import uuidv4 from '@/uuid';
import { mapActions, mapState } from 'vuex';
export default {
    name:"DialogCreateCourse",
    data(){
        return {
            dialogFormVisible: false,
            form: {
                id:uuidv4(),
                courseName: '',
                courseType: '',
                courseTag:'',
                courseDesc: '',
                startDate: '',
                endDate: '',
                classRoomList:[]
            },
            rules: {
                courseName: [
                    { required: true, message: '请输入课程名称', trigger: 'blur' },
                    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                ],
                courseType: [
                    { required: true, message: '请选择课程类型', trigger: 'change' }
                ],
                courseTag: [
                    { required: true, message: '请选择标签', trigger: 'change' }
                ],
                startTime: [
                    { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                ],
                endTime: [
                    { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                ],
                courseDesc: [
                    { required: true, message: '请输入课程描述', trigger: 'change' },
                    { min: 2, max: 180, message: '长度在 2 到 180 个字符', trigger: 'blur' }
                ]
            },
            formLabelWidth: '80px'
        }//
    },
    computed:{
        ...mapState("user",["user"])
    },
    methods:{
        ...mapActions("course_class",["create_course"]),
        openDialog(){
            this.dialogFormVisible=true;
         },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
            if(new Date(this.form.endDate).getTime()<new Date(this.form.startDate).getTime()){
                this.$message({
                    message: '结束时间不能小于开始时间',
                    type: 'error'
                })
                return false;
            }
            if (valid) {
                this.form.id=uuidv4();
                this.create_course({
                    data:{...this.form,teacherId:this.user.id},
                    callback:{
                        success:(msg)=>{
                            this.$message({
                                message: msg,
                                type: 'success'
                            })
                            this.dialogFormVisible=false;
                            this.$refs[formName].resetFields();
                        },
                        error:(msg)=>{
                            this.$message({
                                message: msg,
                                type: 'error'
                            });
                        },
                    }
                })
                
            } else {
                console.log('error submit!!');
                return false;
            }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
</script>

<style>

</style>