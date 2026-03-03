<template>
  <div>
    <span class="add-class" @click="openDialog">
              <el-tooltip class="item" effect="dark" content="新建班级" placement="bottom">
                <i class="el-icon-plus"></i>
              </el-tooltip> 
            </span>

    <!-- div -->
    <el-dialog title="创建班级" :visible.sync="dialogClassRoomFormVisible">
      <div style="padding:0 20px;">
          <el-form :model="form" ref="ruleForm" label-position="left" size="medium" :rules="rules">
              <el-form-item label="班级名称" prop="name" :label-width="formLabelWidth">
                  <el-input v-model="form.name" autocomplete="off"></el-input>
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
export default {
    name:"DialogCreateClassRoom",
    props:["course","user","create_classroom","mutation_CREATE_CLASSROOM"],
    data(){
      return {
        dialogClassRoomFormVisible:false,
        form:{
          classroomId:uuidv4(),
          name:""
        },
        rules:{
          name: [
            { required: true, message: '请输入班级名称', trigger: 'blur' },
            { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
          ],
        },
        formLabelWidth: '80px'
      }
    },
    methods:{
      openDialog(){
        this.dialogClassRoomFormVisible=true;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.form.classroomId=uuidv4();
            this.create_classroom({
                data:{...this.form,teacherId:this.user.id,courseId:this.course.id},
                callback:{
                    success:(msg,data)=>{
                        this.$message({
                            message: msg,
                            type: 'success'
                        })
                        this.dialogClassRoomFormVisible=false;
                        this.$refs[formName].resetFields();
                        let courseId = this.course.id;
                        this.mutation_CREATE_CLASSROOM({courseId,data})
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
.add-class {
  font-size: 18px;
  padding: 10px;
}
.add-class:hover {
  color:blue;
  cursor: pointer;
}
</style>