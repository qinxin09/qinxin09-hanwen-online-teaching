<template>
  <div>
    <el-button @click="showdialog" size="mini">
      <i :size="20" class="el-icon-folder-add"></i>
    </el-button>

    <el-dialog
      :visible.sync="dialogVisible"
      title="操作"
      width="50%"
      custom-class="el-dialog2"
      destroy-on-close
      :modal="false"
      @close="handleCloseDialog"
    >
    <el-input v-if="!optionMode" v-model="creatFolderName" placeholder="输入文件夹名"></el-input>
    
    
    <!-- <span>This is a message</span> -->
    <div v-if="optionMode">
      <el-row :gutter="4" justify="space-around">
        <el-col :span="10">
          <div class="option_div add" @click="shouInput">
              <div>
                <i class="el-icon-folder-add"></i>
              </div>
              <span>新建文件夹</span>
          </div>
        </el-col>
        <el-col :span="10">
         
          <div style="border-radius: 9px;margin:0;padding:0;position: relative;overflow: hidden;text-align: center;">
          <!-- <div v-if="progress" style="position:absolute;height:100%;width:100%;" :style="'background: linear-gradient(to right, green, green '+progress+'%, transparent '+progress+'%); background-size: 100% 100%;'"> -->
          <div v-if="progress" style="position:absolute;height:100%;width:100%;background:#2f3135;" >
            <div style="position:absolute;height:100%;width:100%;" :style="'background: linear-gradient(to right, green, green '+progress+'%, transparent '+progress+'%); background-size: 100% 100%;'">
              <p style="top: 15px; position: relative;color: #ffffff;">{{ progress.toFixed(2) }}%</p>
            </div>
          </div>
            <div class="option_div upload" @click="doUploadFile" >
              <div>
                <i class="el-icon-upload2" :size="20"></i>

              </div>
              <span>上传文件</span>
              <div style="width:0;height:0;margin: 0;padding:0;border: none;overflow: hidden;position:relative">
                
                <input ref="uploadRef" type="file" @change="handleFileChange" />

              </div>
          </div>
        </div>
        </el-col>
      </el-row>
    </div>
    <template #footer>
      <span v-if="optionMode" class="dialog-footer" style="color:#1e82ee;">
        Author: QX
      </span>
      <el-button type="primary" v-if="!optionMode" @click="doCreateFolder" :loading="loading">创建</el-button>
        <el-row v-for="(item,index) in fileList" :key="item.name+index">
          <el-col>
            <div>
              <span style="color:rgb(128, 128, 128);">{{ item.name +"  "+item.size }}</span>
            </div>
          </el-col>
        </el-row>
        <el-button v-show="file!=null" @click="startSubmit">发送</el-button>
        <el-button v-show="progress > 0" @click="cancelUpload">取消</el-button>
    </template>
    
    </el-dialog>
  </div>
</template>
  
<script>
import axios from 'axios';
// import {Interface} from "@/src/global.js";
import {Interface} from "@/global.js";
import uuidv4 from '@/uuid';
export default {
  name: "FolderComponent",
  components: {

  },
  props:["updateDir","getjoinPath","userId"],
  data(){
    return {
      file:null,
      dialogVisible:false,
      optionMode:true,
      creatFolderName:"",
      progress:0,
      uploadRef:null,
      triggerBtn:null,
      fileList:[],
      UPLOAD:{
        action:Interface.upload,
        data:{
          absdir:''
        }
      },
      loading:false,
      // 创建cancel token实例
      source :axios.CancelToken.source()
    }
  },
  methods:{
    handleFileChange(event) {
      this.file = event.target.files[0];
    },
    async startSubmit() {
       
        const CHUNK_SIZE = 1024 * 1024 ; // 1MB
        const TOTAL_CHUNKS = Math.ceil(this.file.size / CHUNK_SIZE);
        
        let currentChunk = 0;
        let filename = uuidv4() +this.file.name;
  
        while (currentChunk < TOTAL_CHUNKS) {
          const start = currentChunk * CHUNK_SIZE;
          const end = Math.min(start + CHUNK_SIZE, this.file.size);
          
          const chunk = this.file.slice(start, end);
  
          const formData = new FormData();
          formData.append('file', chunk);
          formData.append('filename',filename);
          formData.append('chunkIndex', currentChunk);
          formData.append('totalChunks', TOTAL_CHUNKS);
          formData.append('userId',this.userId+""),
          formData.append("path",this.getjoinPath())
  
          await axios.post(Interface.resource.upload, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
            cancelToken: this.source.token,
            onUploadProgress: (progressEvent) => {
              // const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
              // this.progress.chunk = progress;
              this.progress = Math.round(((start+progressEvent.loaded) * 100) / this.file.size);
              // console.log(start,progressEvent.loaded, this.file.size);
            }
          }).then(res => {
            if(res.data.data && res.data.data.state=="complete"){
              this.$message.success(res.data.msg);
              this.dialogVisible=false;
              this.fileList=[];
              this.progress=0;
              this.updateDir();
            }
          }).catch(error => {
            this.$message.error("上传失败");
            if (axios.isCancel(error)) {
              console.log('Request canceled', error.message);
            } else {
              console.error('Upload Error', error);
            }
          });
          currentChunk++;
        }
        
      },

      // 取消上传
      cancelUpload (){
        // 调用cancel方法取消请求
        this.file=null;
        this.source.cancel('Upload canceled by user');
      },
    showdialog () {
      this.dialogVisible = true;
    },
    doCreateFolder() {
      if(this.creatFolderName=="" || this.creatFolderName.length==0){
        return this.$message({
          type: 'warning',
          message: "文件夹名称不能为空"
        })
      }
      this.loading=true;
      // console.log(this.creatFolderName);
      //判断文件夹名称是否合法，检测是否有非法字符
      let reg = /[`~!@#$%^&*()+<>?:"{},.\/;'[\]]/im;
      if(reg.test(this.creatFolderName)){
        this.loading=false;
        return this.$message({
          type: 'warning',
          message: "文件夹名称不能包含非法字符",
        });
      }else{
        axios.get(`${Interface.resource.create_folder}?dir=${this.getjoinPath()}&userId=${this.userId}&name=${this.creatFolderName}`).
        then(res => {
          this.loading=false;
          this.creatFolderName = "";
          if(res.data.code == 200){
            this.$message.success(res.data.msg);
            this.updateDir();
            this.optionMode = true;
            this.dialogVisible = false;
          }else{
            this.$message.error(res.data.msg);
            this.updateDir();
            this.optionMode = false;
          }
        })
        .catch(err => {
          this.loading=false;
          this.creatFolderName = "";
          console.log(err);
        })
      }
    },
    handleCloseDialog (){
      this.dialogVisible=false;
      this.optionMode=true;
    }, 
    doUploadFile ()  {
      // 模拟点击上传组件
      // this.triggerBtn.click();
      // this.uploadRef.handleStart=(rawFile)=>{
      //   console.log("uploadRef");
      //   console.log(rawFile);
      // }
      this.$refs["uploadRef"].click();
      this.$refs["uploadRef"].addEventListener('change', (event) => {
        const selectedFile = event.target.files[0];
        console.log('Selected File:', selectedFile);
        this.fileList.splice(0)
        this.fileList.push(selectedFile);
      });
      
    },
    shouInput () {
      setTimeout(() => {
        this.optionMode = false;
      }, 200);
    }
    
  }
};
</script>

<style>
.el-dialog.el-dialog2 {
  background-color: #ffffff !important;
}
.option_div {
  border: 1px solid rgb(144, 144, 144);
  border-radius: 9px;
  padding: 10px;
  text-align: center;
}
.option_div.add {
  border: 1px solid rgb(11, 89, 173);
}
.option_div.upload {
  border: 1px solid rgb(171, 119, 8);
}
.option_div:hover {
  background-color: rgb(247, 247, 247);
}
</style>