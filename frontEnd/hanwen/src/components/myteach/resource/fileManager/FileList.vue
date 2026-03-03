<template>
    <div class="container">
        <div class="file-header">
            <div>
                <el-page-header @back="goBack">
                    <template #icon>
                        <span>
                            <svg t="1694248500726" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4090" width="200" height="200"><path d="M800 480H268.8l233.6-233.6c12.8-12.8 12.8-32 0-44.8-12.8-12.8-32-12.8-44.8 0l-284.8 288c-12.8 12.8-12.8 32 0 44.8h3.2l284.8 288c6.4 6.4 16 9.6 22.4 9.6 9.6 0 16-3.2 22.4-9.6 12.8-12.8 12.8-32 0-44.8L272 544H800c19.2 0 32-12.8 32-32s-16-32-32-32z" fill="#666666" p-id="4091"></path></svg>
                        </span>
                    </template>
                    <template #title >
                        <span style="font-size: 16px;line-height:40px;color: white;"> 主页 </span>
                    </template>
                    <template #extra>
                        <div class="flex items-center">
                            <DialogComponent :updateDir="updateDir" :getjoinPath="getjoinPath" ></DialogComponent>
                        </div>
                    </template>
                </el-page-header>
                <el-breadcrumb :separator-icon="ArrowRight">
                    
                    <el-breadcrumb-item @click="goRoot">home</el-breadcrumb-item>
                    <el-breadcrumb-item v-for="(p,index) in path" :key="p"  @click="jumpPath(index) ">{{ p }}</el-breadcrumb-item>

                </el-breadcrumb>

                <audio id="audio" ref="player" style="display: none" loop autoplay></audio>

                <div style="margin: 12px 0px ;">
                    <el-row :gutter="4" justify="space-between" class="sd">
                        <el-col :span="6">
                            <el-select v-if="ostype.toLowerCase().match('window') != null" v-model="root" placeholder="Select" size="small" @change="goRoot">
                                <el-option
                                    v-for="item in ['C:/','D:/','E:/','F:/','G:/']"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                                    />
                            </el-select>
                            <el-select v-else v-model="root" placeholder="Select" size="small" @change="goRoot">
                                <el-option
                                    v-for="item in ['/storage/emulated/0/','/storage/emulated/','/storage/']"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                                    />
                            </el-select>
                        </el-col>
                        <el-col :span="6">
                            <el-select v-model="sortWays" placeholder="Select" size="small" @change="sortMethod">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                    :width="10"
                                    />
                            </el-select>
                        </el-col>
                        <el-col :span="6">
                            <el-input type="text" placeholder="搜索" size="small" v-model="filter_key" clearable></el-input>
                        </el-col>
                    </el-row>
                </div>
            </div>
        </div>
        <div class="filelist" 
        v-loading="loading.state"
        :element-loading-text="loading.text"
        :element-loading-background="loading.background"
        >
            <div>
                <el-scrollbar >
                    <el-row class="row-bg" justify="space-between" @click="handleList('parent','')">
                        <el-col :span="4">
                            <div  class="grid-content ep-bg-purple file-icon" >
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
                    <el-row class="row-bg" justify="space-between" v-for="(item,index) in (filter_key==''?state.folders:state.folders_filter)" :key="'folders'+index" >
                        <el-col :span="4"  @click="handleList('dir',item.name)">
                            <div  class="grid-content ep-bg-purple file-icon" >
                                <img src="@/assets/folder-icon.png" style="width: 40px;">
                            </div>
                        </el-col>
                        <el-col :span="17" @click="handleList('dir',item.name)">
                            <div  class="grid-content ep-bg-purple" style="border-bottom:1px solid #333333">
                                <div class="file-text ">
                                    <div class="file-name">{{ item.name }}</div>
                                </div>
                                <span class="file-text">{{ getDateTime(item.mtime) }}</span>
                            </div>
                        </el-col>
                        <el-col :span="3">
                            <div  class="grid-content ep-bg-purple file-checkbox" >
                                <el-checkbox></el-checkbox>
                            </div>
                        </el-col>
                    </el-row>
                    <el-row class="row-bg" justify="space-between" v-for="(item,index) in (filter_key==''?state.files:state.files_filter)" :key="'files'+index" >
                        <el-col :span="4" @click="downloadFile((root+path.join('/')),item.name)">
                            <div  class="grid-content ep-bg-purple file-icon"  >
                                <!-- <img :src="getIcon(item.name)" style="width: 40px;"> -->
                                <!-- 判断文件类型 --> 
                                <!-- <img v-if="item.name.endsWith('.zip')" v-bind:src="'~@/assets/'+'doc.png'" style="width: 40px;"> -->
                                <!-- <img v-else src="~@/assets/file-other.png" style="width: 40px;"> -->
                                <!-- <img src="../../assets/file-other.png" alt="Dynamic Image"> -->
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
                        <el-col :span="17" @click="handleList('file',item.name)">
                            <div  class="grid-content ep-bg-purple" style="border-bottom:1px solid #333333">
                                <div class="file-text ">
                                    <div class="file-name">{{ item.name }}</div>
                                </div>
                                <span class="file-text">{{ getDateTime(item.mtime) +"    "+ getSize(item.size) }}</span>
                            </div>
                        </el-col>
                        <el-col :span="3">
                            <div  class="grid-content ep-bg-purple file-checkbox" >
                                <el-checkbox></el-checkbox>
                            </div>
                        </el-col>
                    </el-row>
                </el-scrollbar>
            </div>
        </div>
    </div>
</template>

<script>
import { ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import axios from 'axios';
import api from '@/api'
import qx from '@/qx';
import { store } from '@/store/player.js'
import router from '@/router';

import DialogComponent from './DialogComponent.vue'
export default {
    name:"FileList",
    components: {
        DialogComponent
    },
    mounted(){
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
        }
    },
    setup(){
        const player = ref(null);
        const value = ref('');
        var ostype = ref('');
        const root = ref('/storage/emulated/0/');
        
        
        const path = ref([]);
        var filter_key = ref('');
        var loading = reactive({
            state:true,
            text:'加载中...',
            background:'#000000'
        });
        const getjoinPath = ()=>{
            if(path.value.length==0){
                return root.value;
            }else{
                return root.value+path.value.join('/')+"/"
            }
        }
        const handleSelectFile = (name)=>{
            let isMusic=false;
            let musicType=[".mp3",".flac","wav",".m4a",".ogg"];
            for(let i=0;i<musicType.length;i++){
                if(name.endsWith(musicType[i])){
                    
                    if(store.name != name ){
                        // 非同一首歌，暂停，并显示加载，加载
                        //判断是否正在加载？
                        if(store.loading_){
                            return ElMessage.error('正在加载中，请稍后再试');
                        }
                        store.name = name;
                        store.restart=true;
                        store.pause();
                    }else{
                        //同一首歌，自动切换播放暂停
                        store.play();
                        return ;
                    }
                    playMusic(root.value+path.value.join('/')+"/"+name);
                    return isMusic=true;
                }
            }
            if(isMusic==false){
                ElMessage.info("准备下载");
                console.log("path",path.value);
                console.log("下载文件",root.value+path.value.join('/')+"/"+name);
                
                return downloadFile(root.value+path.value.join('/'),name);
            }
    }
        
        const options = [
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
        ];
        var sortWays=ref(options[0].value);
        // 定义响应式对象folders和files
        var state = reactive({
            folders: [],
            files: [],
            folders_filter:[],
            files_filter:[]
        });

        const goBack = ()=> {
            // location.href="/"
            router.push({path:'/'});
        }

        const sortMethod = ()=>{
            if(sortWays.value=="name"){
                console.log("按名称排序");
                state.folders.sort((a, b) => {
                    const nameA = a.name[0].localeCompare(b.name[0], 'zh');
                    return nameA;
                });
                state.files.sort((a, b) => {
                    const nameA = a.name[0].localeCompare(b.name[0], 'zh');
                    return nameA;
                });
            }else if(sortWays.value=="size"){
                console.log("按大小排序")
                state.folders.sort((a, b) => {
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
                state.files.sort((a, b) => {
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
                state.folders.sort((a, b) => {
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
                state.files.sort((a, b) => {
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
        }
        

        // 发请求获取文件信息
        const getDir = (url) => {
            loading.state=true;
            state.folders.splice(0);
            state.files.splice(0);

            
            loading.text=root.value + path.value.join('/');
            axios.get(url).then(
                res => {
                    if (res.data.code == 200) {
                        
                        state.folders = res.data.data.folders;
                        state.files = res.data.data.files;
                        // console.log(state.folders, state.files);
                        sortMethod();
                        console.log("排序")
                        // console.log(state.folders, state.files);
                        loading.state=false;
                    }
                },
                err => {
                    ElMessage.error(err.response.data);
                    loading.state=false;
                    console.log(err);
                }
            ).catch(e=>{
                console.log("出错了"+e);
            })
        };
        const updateDir=()=>{
            let url = `${api.getDir}?dir=${root.value}${path.value.join('/')}`;
            getDir(url);
        }
        
        const downloadFile = (path,name)=>{

            const fullpath = path +"/"+name;
            const encodedPath = encodeURIComponent(fullpath);
            
            console.log("下载文件",api.getdownload+"?fullpath="+encodedPath);
            const link = document.createElement('a');
            link.href = api.getdownload+"?fullpath="+encodedPath;
            link.setAttribute('download', name); // 设置下载文件的文件名
            link.click();
            // axios.get(api.getdownload+"?fullpath="+fullpath)

        }
        const getDateTime=(dateStr)=>{
            return qx.date(dateStr).format('yyyy-MM-dd HH:mm:ss');
            // return qx.date(dateStr)+" "+qx.time(dateStr);
        }
        const getSize = (byte) => {
            if (byte < 1024) {
                return byte + ' B';
            } else if (byte < 1024 * 1024) {
                return (byte / 1024).toFixed(2) + ' KB';
            } else if (byte < 1024 * 1024 * 1024) {
                return (byte / (1024 * 1024)).toFixed(2) + ' MB';
            } else {
                return (byte / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
            }
        }
        console.log(store);

        const playMusic = (fullpath) => {
                
            const encodedPath = encodeURIComponent(fullpath);
            const downloadUrl = `${api.getPlay}?fullpath=${encodedPath}`;
            /**
             * 通过调用 encodeURIComponent() 函数对 fullpath 进行编码，
             * 并将编码后的值赋给 encodedPath 变量。然后，
             * 将 encodedPath 变量包含在 URL 中，确保路径中的特殊字符得到正确处理。
                这样修改后，downloadUrl 将包含经过编码的路径，可以避免由于特殊字符导致的路径错误。
             */
            // 播放音频
            store.src = downloadUrl;
            store.play();
                
        };

        
        const handleList=(type,name)=>{
            switch(type){
                case "parent":
                    if(path.value.length>0){
                        path.value.pop();
                        filter_key.value='';
                        console.log("清除过滤")
                        return updateDir();
                    }else{
                        ElMessage.warning("已经是根目录了");
                        return;
                    }
                case "dir":
                    filter_key.value='';
                    console.log("清除过滤")
                    path.value.push(name);
                    return updateDir();
                case "file":
                    return handleSelectFile(name);
                    
                    
            }
        }

        const getOSType = async () => {
            await axios.get(api.getOSType).then(
                res => {
                    if (res.data.toLowerCase().match("windows")) {
                        ostype.value = res.data;
                        console.log(ostype.value);
                        root.value = 'D:/';
                    }
                    updateDir();
                },
                (err) => {
                    console.log(err);
                    loading.state=false;
                    ElMessage.error("获取os失败,检查服务器！");
                }
            )
         
        }

        const unknownType = (name)=>{
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
        }
        const jumpPath= (index)=>{
            if(index==path.value.length-1){
                return;//就是当前路径，不需要跳转
            }
            path.value=path.value.slice(0,index+1);
            // console.log(path.value);
            filter_key.value='';
            console.log("清除过滤");
            return updateDir();
            
        }
        const goRoot= ()=>{
            filter_key.value='';
            path.value.splice(0);
            return updateDir();
        }
       
        return {
            goBack,
            ArrowRight,
            value,
            options,
            state,
            unknownType,
            getDir,
            getOSType,
            ostype,
            root,
            path,
            updateDir,
            loading,
            handleList,
            jumpPath,
            goRoot,
            getDateTime,
            getSize,
            sortWays,
            sortMethod,
            filter_key,
            player,
            downloadFile,
            DialogComponent,
            getjoinPath
        }
    }
}
</script>

<style scoped>
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
        height: 80vh;
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
        background-color: #1e2b3d;
    }
    *{
        user-select: none;
        -moz-user-select: none;
        -webkit-user-select: none;
    }
</style>