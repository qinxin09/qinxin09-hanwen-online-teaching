<template>
    <div style="display: inline-block;margin:0 10px;" @click.prevent="">
        <el-button title="编辑班级信息" icon="el-icon-edit" :circle="true" type="plain" size="mini" @click.prevent="openDialog"></el-button>
      <!-- div -->
      <el-dialog @open="form.name=classroom.name" title="更新班级信息" :visible.sync="dialogClassRoomFormUpdateVisible" :destroy-on-close="true">
        <div style="padding:0 20px;">
            <el-form :model="form" ref="ruleForm" label-position="left" size="medium" :rules="rules">
                <el-form-item label="班级名称" prop="name" :label-width="formLabelWidth">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确认</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script>
  export default {
      name:"DialogUpdateClassRoom",
      props:["getClassRoom","update_classroom","mutation_UPDATE_CLASSROOM","courseIndex","roomIndex"],
      data(){
        return {
          dialogClassRoomFormUpdateVisible:false,
          form:{
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
      computed:{
        classroom(){
          return this.getClassRoom(this.courseIndex,this.roomIndex) || {};
        }
      },
      methods:{
        openDialog(){
          console.log(this.classroom)
          this.dialogClassRoomFormUpdateVisible=true;
        },
        submitForm(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              const classroomData = {...this.classroom ,name:this.form.name};
              this.update_classroom({
                  data:classroomData,
                  callback:{
                      success:(msg)=>{
                          this.$message({
                              message: msg,
                              type: 'success'
                          })
                          this.dialogClassRoomFormUpdateVisible=false;
                          this.$refs[formName].resetFields();
                          const d={
                            courseId:this.classroom.courseId,
                            classroomId:this.classroom.classroomId,
                            classroomData:classroomData
                          }
                          this.mutation_UPDATE_CLASSROOM(d);
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