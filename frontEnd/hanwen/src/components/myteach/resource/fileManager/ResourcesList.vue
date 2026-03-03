<template>
  <div>
    <div class="file-header">
            <div>         
                <div style="margin: 12px 0px ;">
                    <div style="width:100%;">
                        <el-row  justify="space-between" class="ignore">
                            <el-col :span="8">
                                <el-select v-model="sortWays" placeholder="排序方式" size="small" @change="sortMethod">
                                    <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                        :width="10"
                                        />
                                </el-select>
                            </el-col>
                            <el-col :span="8">
                                <el-input type="text" placeholder="搜索" size="small" v-model="filter_key" clearable></el-input>
                            </el-col>
                            <el-col :span="8">
                                <div style="display:flex;justify-content:center;">
                                    <DialogComponent :updateDir="updateDir" :getjoinPath="getjoinPath" :userId="user.id"></DialogComponent>
                                </div>
                            </el-col>
                        </el-row>
                    </div>

                    <div class="filelist" 
                        v-loading="loading.state"
                        :element-loading-text="loading.text"
                        element-loading-background="rgba(0, 0, 0, 0.3)"
                    >


                    <div class="myfileListcontent" style="border:1px solid rgb(210 210 210);margin-top:10px;overflow-y:scroll;">
                        <div class="list-items first"  @click="handleList('parent','')">
                            <el-row >
                                <el-col :span="4" >
                                    <div @click="alert()" class="grid-content ep-bg-purple file-icon" >
                                        <img src="@/assets/folder-icon.png" style="width: 40px;">
                                    </div>
                                </el-col>
                                <el-col :span="17">
                                    <div  class="grid-content ep-bg-purple" style="display: flex; justify-content: left; align-items: center;">
                                        <div class="file-text ">
                                            <div class="file-name">上一级</div>
                                        </div>
                                    </div>
                                </el-col>
                                <el-col :span="3"> <div>
                                    &nbsp;
                                </div> </el-col>
                            </el-row>
                        </div>
                        <!-- 循环文件夹 -->
                        <div class="list-items" v-for="(item,index) in (filter_key==''?state.folders:state.folders_filter)" :key="'folders'+index" >
                            <el-row >
                                <el-col :span="4" @click.native="handleList('dir',item.name)" >
                                    <div class="grid-content ep-bg-purple file-icon" >
                                        <img src="@/assets/folder-icon.png" style="width: 40px;">
                                    </div>
                                </el-col>
                                <el-col :span="17" @click.native="handleList('dir',item.name)" >
                                    <div  class="grid-content ep-bg-purple" style="border-bottom:1px solid #333333">
                                        <div class="file-text ">
                                            <div class="file-name">{{ item.name }}</div>
                                        </div>
                                        <span class="file-text">{{ getDateTime(item.mtime) }}</span>
                                    </div>
                                </el-col>
                                <el-col :span="3">
                                    <div  class="grid-content ep-bg-purple file-checkbox" >
                                        <el-button-group>
                                            <el-button type="primary" icon="el-icon-edit" @click.prevent="filesOption(item,'folder','rename')"></el-button>
                                            <el-button type="danger" icon="el-icon-delete" @click.prevent="filesOption(item,'folder','delete')"></el-button>
                                        </el-button-group>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>

                        <!-- 循环文件 -->
                        <div class="list-items" >
                            <el-row class="row-bg" justify="space-between" v-for="(item,index) in (filter_key==''?state.files:state.files_filter)" :key="'files'+index" >
                                <el-col :span="4" @click.native="clickFile(item)">
                                    <div  class="grid-content ep-bg-purple file-icon"  >
                                        <img v-if="item.name.endsWith('.doc') || item.name.endsWith('.docx')" src="~@/assets/doc.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.rar')" src="~@/assets/rar.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.txt')" src="~@/assets/txt.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.svg')" src="~@/assets/svg.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.jpg') || item.name.endsWith('.jpeg')" src="~@/assets/doc.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.ppt')" src="~@/assets/ppt.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.png')" src="~@/assets/png.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.pdf')" src="~@/assets/pdf.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.xlsx')" src="~@/assets/xlsx.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.mp4')" src="~@/assets/mp4.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.zip')" src="~@/assets/zip.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.mp3')||item.name.endsWith('.flac')||item.name.endsWith('.aac')||item.name.endsWith('.m4a')||item.name.endsWith('.wav')" src="~@/assets/music.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.gif')" src="~@/assets/gif.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.sql')" src="~@/assets/sql.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.md')||item.name.endsWith('.MD')" src="~@/assets/md.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.java')" src="~@/assets/java.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.js')" src="~@/assets/js.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.json')" src="~@/assets/json.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.css')" src="~@/assets/css.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.yaml')||item.name.endsWith('.yml')" src="~@/assets/yaml.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.html')" src="~@/assets/html.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.exe')" src="~@/assets/exe.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.apk')" src="~@/assets/apk.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.py')" src="~@/assets/py.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.c')||item.name.endsWith('.cpp')" src="~@/assets/c.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.xml')" src="~@/assets/xml.png" style="width: 40px;">
                                        <img v-if="item.name.endsWith('.xls')" src="~@/assets/xls.png" style="width: 40px;">
                                        <img v-if="unknownType(item.name)" src="~@/assets/file-other.png" style="width: 40px;">
                                        
                                    </div>
                                </el-col>
                                <el-col :span="17" @click.native="clickFile(item)">
                                    <div  class="grid-content ep-bg-purple" style="border-bottom:1px solid #333333">
                                        <div class="file-text ">
                                            <div class="file-name">{{ item.name.substring(36) }}</div>
                                        </div>
                                        <span class="file-text">{{ getDateTime(item.mtime) +"    "+ getSize(item.size) }}</span>
                                    </div>
                                </el-col>
                                <el-col :span="3">
                                    <div  class="grid-content ep-bg-purple file-checkbox" >
                                        <el-button-group>
                                            <el-button type="primary" icon="el-icon-edit" @click.prevent="filesOption(item,'file','rename')"></el-button>
                                            <el-button v-if="false" type="primary" icon="el-icon-download" @click.prevent="filesOption(item,'file','download')"></el-button>
                                            <el-button type="danger" icon="el-icon-delete" @click.prevent="filesOption(item,'file','delete')"></el-button>
                                        </el-button-group>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>
                    </div>
            
        </div>

                </div>
            </div>
        </div>

        <!-- 重命名对话框 -->
        <el-dialog
            title="重命名"
            :visible.sync="renameDialogVisible"
            width="30%">
            <div style="padding:10px;">
                <el-input placeholder="新名称" v-model="newFileName">
                    <template v-if="fileNameExtension.length>1" slot="append">{{ fileNameExtension }}</template>
                </el-input>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button @click="renameDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="renameFile">确 定</el-button>
            </span>
        </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import DialogComponent from './DialogComponent.vue';
import {Interface} from '@/global.js';
import qx from '@/qx.js';
import axios from 'axios';
export default {
    name:"ResourcesList",
    props:["callbackConfig"],
    components: {
        DialogComponent
    },
    mounted(){
        console.log("this is Resources list")
        //拿到教师id 
        if(this.user!=null){
            this.courseId = this.user.id;
        }else{
            console.error("请先登录");
        }
        //获取根目录下的所有文件及文件夹
        console.log('加载文件');
        this.getOSType();
    },
    watch:{
        filter_key:{
            handler(newValue){
                if(newValue == ''){
                    console.log('清除过滤');
                }else{
                    console.log('过滤文件');
                    this.state.folders_filter=this.state.folders.filter(item=>{
                        if(item.name.toLowerCase().match(newValue.toLowerCase()) != null){
                            return item;
                        }
                    });
                    this.state.files_filter=this.state.files.filter(item=>{
                        if(item.name.toLowerCase().match(newValue.toLowerCase()) != null){
                            return item;
                        }
                    });
                }
            }
        },
        state:{
            immediate:true,
            deep:true,
            handler(newValue){
                console.log(newValue);
                // if(newValue.folders_filter.length == 0 && newValue.files_filter.length == 0){
                //     this.state.folders_filter=this.state.folders;
                //     this.state.files_filter=this.state.files;
                // }
            }
        }
    },
    data(){
        return {
            renameDialogVisible:false,
            player:null,
            value:"",
            ostype:"",
            root:"/",
            path:[],
            filter_key:"",
            newFileName:"",
            fileNameExtension:"",
            currentFileUUID:"",
            currentSelectName:"",
            loading:{
                state:true,
                text:"加载中...",
                background:'#000000'
            },
            options : [
                {
                    value: 'name',
                    label: '名称',
                },
                {
                    value: 'time',
                    label: '时间',
                },
                {
                    value: 'size',
                    label: '大小',
                }
            ],
            sortWays:'name',
            state:{
                folders:[],
                files:[],
                folders_filter:[],
                files_filter:[]
            }
            
        }
    },
    
    computed:{
        ...mapState("user",['user'])
    },
    methods:{
        clickFile(item){
            console.log(this.callbackConfig);
            if(this.callbackConfig){
                
                console.log("窗口文件选择模式，拦截正常逻辑",item.name.substring(0,36))
                this.$message.success("文件选择成功");
                const data = {
                    name:item.name,
                    path:this.getjoinPath(),
                    teacherId:this.user.id
                }
                console.log("pring file info",data);
                return this.$bus.$emit("selected-file",data);
            }
            console.log("浏览模式，正常逻辑");
            console.log("点击了文件,逻辑...");
        },
        filesOption(item,type,operator){
            this.currentSelectName=item.name;
            let path = this.getjoinPath();
            let url="";
            if(operator=='download'){
                alert("开发中")
            }else if(operator=='delete'){
                url=`${Interface.resource.delete}?userid=${this.user.id}&dir=${path+item.name}`;
                this.deleteFile(url,item.name,type);
            }else if(operator=='rename'){
                this.renameFileShowDialog(type);
            }
        },
        // renameFileShowDialog(getUrl){
        //     this.$prompt('请输入文件名称', '提示', {
        //         confirmButtonText: '确定',
        //         cancelButtonText: '取消',
        //         //文件名允许数字、字母、下划线、中文、-、空格
        //         inputPattern: /^[a-zA-Z0-9\u4e00-\u9fa5-_ ]+$/,
        //         inputErrorMessage: '文件名不能包含特殊字符'
        //         }).then(({ value }) => {
        //             axios.get(getUrl(value)).then(
        //                 res=>{
        //                     if(res.data.code==200){
        //                         this.$message.success(res.data.msg);
        //                         this.updateDir();
        //                     }else{
        //                         this.$message.error(res.data.msg);
        //                     }
        //                 }
        //             )
        //         }).catch(() => {
        //             console.log("取消重命名");
        //         });
        // },
        renameFileShowDialog(type){
            this.newFileName=this.currentSelectName+"";
            if(type=='folder'){
                this.currentFileUUID="";
                this.fileNameExtension="";
            }
            if(type=='file'){
                this.currentFileUUID=(this.newFileName+"").substring(0,36);
                this.fileNameExtension="."+this.getFileNameExtension(this.newFileName.substring(36));
                this.newFileName=this.newFileName.substring(36,(this.newFileName.length - this.fileNameExtension.length));
            }
            this.renameDialogVisible=true;
        },
        renameFile(){
            if(this.newFileName.length<1){
                return this.$message.error("输入不能为空");
            }
            if(this.newFileName.indexOf('.')!=-1 && this.fileNameExtension.length==0){
                return this.$message.error("输入不能包含.");
            }
            let path = this.getjoinPath();
            axios.get(`${Interface.resource.rename}?userid=${this.user.id}&newname=${this.currentFileUUID+this.newFileName+this.fileNameExtension}&dir=${path+this.currentSelectName}`).then(
                res=>{
                    if(res.data.code==200){
                        this.$message.success(res.data.msg);
                        this.updateDir();
                        this.renameDialogVisible = false;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                }
            ).catch(() => {
                console.log("取消重命名");
            });
        },
        
        getFileNameExtension(fileName) {
            const parts = fileName.split(".");
            if (parts.length > 1) {
                return parts[parts.length - 1];
            } else {
                return "";
            }
        },
        deleteFile(url,name,type){
            let msg = `此操作将永久删除该文件`;
            let fileName = name;
            if(type=="file"){
                fileName = name.substring(36);
            }else{
                msg+="夹"
            }
            this.$confirm(`${msg}[${fileName}], 是否继续?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.get(url).then(
                    res=>{
                        if(res.data.code==200){
                            this.$message.success(res.data.msg);
                            this.updateDir();
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    }
                )
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        },
        goBack(){
            return this.$router.go(-1);
        },
        getjoinPath(){
            if(this.path.length==0){
                return this.root;
            }else{
                return this.root+this.path.join('/')+"/"
            }
        },
        sortMethod(){
            if(this.sortWays=="name"){
                console.log("按名称排序");
                this.state.folders.sort((a, b) => {
                    const nameA = a.name[0].localeCompare(b.name[0], 'zh');
                    return nameA;
                });
                this.state.files.sort((a, b) => {
                    const nameA = a.name.substring(36)[0].localeCompare(b.name.substring(36)[0], 'zh');
                    return nameA;
                });
            }else if(this.sortWays=="size"){
                console.log("按大小排序")
                this.state.folders.sort((a, b) => {
                    const sizeA = a.size;
                    const sizeB = b.size;

                    if (sizeA < sizeB) {
                        return -1;
                    }
                    if (sizeA > sizeB) {
                        return 1;
                    }
                    return 0;
                });
                this.state.files.sort((a, b) => {
                    const sizeA = a.size;
                    const sizeB = b.size;

                    if (sizeA < sizeB) {
                        return -1;
                    }
                    if (sizeA > sizeB) {
                        return 1;
                    }
                    return 0;
                });
            }else{
                console.log("按时间排序")
                this.state.folders.sort((a, b) => {
                    const timeA = new Date(a.mtime);
                    const timeB = new Date(b.mtime);

                    if (timeA > timeB) {
                        return -1;
                    }
                    if (timeA < timeB) {
                        return 1;
                    }
                    return 0;
                });
                this.state.files.sort((a, b) => {
                    const timeA = new Date(a.mtime);
                    const timeB = new Date(b.mtime);

                    if (timeA > timeB) {
                        return -1;
                    }
                    if (timeA < timeB) {
                        return 1;
                    }
                    return 0;
                });
            }
        },
        getDir(url){
            this.loading.state=true;
            this.state.folders.splice(0);
            this.state.files.splice(0);

            
            // this.loading.text=this.root + this.path.join('/');
            axios.get(url).then(
                res => {
                    if (res.data.code == 200) {
                        console.log("获取到的数据：",res.data)
                        this.state.folders = res.data.data.folders;
                        this.state.files = res.data.data.files;
                        // console.log(state.folders, state.files);
                        console.log("排序")
                        this.sortMethod();
                        this.loading.state=false;
                    }
                },
                err => {
                    this.$message.error(err.response.data);
                    this.loading.state=false;
                    console.log(err);
                }
            ).catch(e=>{
                console.log("出错了"+e);
            })
        },
        updateDir(){
            let url = `${Interface.resource.get_resource}?userid=${this.user.id}&dir=${this.root}${this.path.join('/')}`;
            console.log("url ",url)
            this.getDir(url);
        },
        downloadFile(path,name){

            const fullpath = path +"/"+name;
            const encodedPath = encodeURIComponent(fullpath);

            console.log("下载文件",Interface.resource.getdownload+"?fullpath="+encodedPath);
            const link = document.createElement('a');
            link.href = Interface.resource.getdownload+"?fullpath="+encodedPath;
            link.setAttribute('download', name); // 设置下载文件的文件名
            link.click();
            // axios.get(Interface.resource.getdownload+"?fullpath="+fullpath)

        },
        getDateTime(dateStr){
            return qx.date(dateStr).format('yyyy-MM-dd HH:mm:ss');
            // return qx.date(dateStr)+" "+qx.time(dateStr);
        },
        getSize  (byte)  {
            if (byte < 1024) {
                return byte + ' B';
            } else if (byte < 1024 * 1024) {
                return (byte / 1024).toFixed(2) + ' KB';
            } else if (byte < 1024 * 1024 * 1024) {
                return (byte / (1024 * 1024)).toFixed(2) + ' MB';
            } else {
                return (byte / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
            }
        },
        playMusic(){

        },
        handleList(type,name){
            switch(type){
                case "parent":
                    if(this.path.length>0){
                        this.path.pop();
                        this.filter_key='';
                        console.log("清除过滤")
                        return this.updateDir();
                    }else{
                        this.$message.warning("已经是根目录了");
                        return;
                    }
                case "dir":
                    this.filter_key='';
                    console.log("清除过滤")
                    this.path.push(name);
                    return this.updateDir();
                case "file":
                    return handleSelectFile(name);
                    
                    
            }
        },
        getOSType(){
            this.root = '/';
            this.updateDir();
        },
        unknownType(name){
            let ends = name.substring(name.lastIndexOf('.') + 1);
            switch(ends){
                case 'aac':
                    return false;
                case 'apk':
                    return false;
                case 'c':
                    return false;
                case 'cpp':
                    return false;
                case 'css':
                    return false;
                case 'doc':
                    return false;
                case 'docx':
                    return false;
                case 'exe':
                    return false;
                case 'flac':
                    return false;
                case 'gif':
                    return false;
                case 'html':
                    return false;
                case 'java':
                    return false;
                case 'jpg':
                    return false;
                case 'js':
                    return false;
                case 'json':
                    return false;
                case 'md':
                    return false;
                case 'MD':
                    return false;
                case 'mp4':
                    return false;
                case 'mp3':
                    return false;
                case 'm4a':
                    return false;
                case 'pdf':
                    return false;
                case 'png':
                    return false;
                case 'ppt':
                    return false;
                case 'py':
                    return false;
                case 'rar':
                    return false;
                case 'sql':
                    return false;
                case 'svg':
                    return false;
                case 'wav':
                    return false;
                case 'txt':
                    return false;
                case 'xls':
                    return false;
                case 'xlsx':
                    return false;
                case 'xml':
                    return false;
                case 'yaml':
                    return false;   
                case 'yml':
                    return false;
                case 'zip':
                    return false;
                default:
                    return true;
            }
        },
        jumpPath(index){
            if(index==this.path.length-1){
                return;//就是当前路径，不需要跳转
            }
            this.path=this.path.slice(0,index+1);
            // console.log(path.value);
            this.filter_key='';
            console.log("清除过滤");
            return this.updateDir();
            
        },
        goRoot(){
            this.filter_key='';
            this.path.splice(0);
            return this.updateDir();
        }
    }
}
</script>
<style scoped>
    .list-items.first{
        
    }
    .list-items{
        cursor: pointer;
    }



    .container{
        /* height: 100vh; */
    }
    ul{
        height: 300px;
        padding: 0;
        margin: 0;
        list-style: none;
    }
   
  
    path{
        fill: rgb(184, 184, 184);
    }
    svg{
        width: 30px;
        height: 25px;
        display: flex;
    }

    

    .file-header > div{
        /* overflow: hidden; */
        padding: 14px;
        padding-bottom: 0;
    }
    .filelist {
        height: 67vh;
    }
    .filelist > div{
        /* padding: 15px 0px ; */
        height: 100%;
    }
    
    .el-row {
        margin: 10px 10px;
        /* border: 1px solid #e8e8e8; */
        /* border-bottom: 1px solid gray; */
    }
    .grid-content {
        /* border */
        /* border-radius: 4px; */
        /* min-height: 36px; */
        /* border: 1px solid #e8e8e8; */
        height: 100%;
    }
    .file-icon,.file-checkbox{
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .file-text{
        display: flex;
        font-size:12px;
        color:#8b8b8b;
    }
    .file-name{
        color: rgb(121, 121, 121);
        width: 100%;
        word-break: break-word;
        text-align: left;
        overflow: hidden;
        height: 20px;
        font-size: 14px;
        /* border-bottom: 1px solid white; */
    }
    .el-breadcrumb {
        cursor: pointer;
    }
    .container{
        user-select: none;
    }
    .el-row:hover {
        background-color: hsl(240, 1%, 85%);
    }
    .el-row.ignore:hover {
        background-color:rgb(255, 255, 255);
    }
    *{
        user-select: none;
        -moz-user-select: none;
        -webkit-user-select: none;
    }
    
</style>
    