<template>
  <div>
    <el-button icon="el-icon-plus" @click="open">创建公告</el-button>
    <!-- div -->
    <el-dialog ref="ruleForm" :title="dialogTitle" :visible.sync="dialogNoticeFormVisible" @close="closeDialog('ruleForm')"> 
      <div style="padding:0 20px;">
          <el-form :model="form" ref="ruleForm" label-position="left" size="medium" :rules="rules">
              <el-form-item label="标题" prop="title" :label-width="formLabelWidth">
                  <el-input v-model.trim="form.title" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="内容" prop="content" :label-width="formLabelWidth">
                  <el-input :autosize="{ minRows: 2, maxRows: 6 }" type="textarea" v-model="form.content" autocomplete="off"></el-input>
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

export default {
    name:"DialogCreateDialog",
    data(){
      return {
        dialogNoticeFormVisible:false,
        mode:"create",
        dialogTitle:"",
        form:{
          title:"",
          content:""
        },
        rules:{
          title:[
            { required: true, message: '请输入标题', trigger: 'blur' }
          ],
          content:[
            { required: true, message: '请输入内容', trigger: 'blur'}
          ]
        },
        formLabelWidth:"50px"
      }
    },
    mounted(){
      this.$bus.$on("edit-notice", (datasource)=>{
        this.form=JSON.parse(JSON.stringify(datasource));
        this.dialogTitle="编辑公告";
        this.mode="update",
        this.dialogNoticeFormVisible=true;
      });
      this.$bus.$on("saved-notice-success",()=>{
        this.dialogNoticeFormVisible=false;
      })
    },
    beforeDestroy(){
      this.$bus.$off("edit-notice");
      this.$bus.$off("saved-notice-success");
    },
    methods:{

      closeDialog(formName){
        this.$refs[formName].resetFields();
      },
      open(){
        this.dialogTitle="创建公告";
        this.mode="create",
        this.form={
          title:"",
          content:""
        }
        this.dialogNoticeFormVisible=true;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.mode=="create"){
              this.$bus.$emit("create-notice", JSON.parse(JSON.stringify(this.form)));
            }else if(this.mode=="update"){
              this.$bus.$emit("update-notice", JSON.parse(JSON.stringify(this.form)));
            }
          }
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
}
</script>

<style>

</style>