<template>
    <div>
      <div>
        </div>
        <el-drawer
            title="附件资料"
            size="80%"
            :visible.sync="drawer"
            :direction="direction"
            :before-close="handleClose">
            <!-- <span>{{dataResource}}</span> -->
            <div>
              <!-- <qx-grid :gridDataSource="gridDataSource"></qx-grid> -->
            </div>
            <!-- <PreView></PreView> -->

            <el-col :sm="12" :lg="6" v-if="isError">
              <el-result icon="error" title="资源不存在" subTitle="该课件已丢失！">
                <!-- <template slot="extra">
                  <el-button type="primary" size="medium">返回</el-button>
                </template> -->
              </el-result>
            </el-col>

            <iframe v-if="pdfUrl.length>0" :src="pdfUrl" style="width: 100%; height: 600px;"></iframe>
        </el-drawer>
    </div>
</template>

<script>
import { Interface } from '@/global';
import axios from 'axios';
// import QxGrid from './qxGrid.vue'
// import PreView from './preView2.vue';

  export default {
    name:"ShowDrawer",
    components:{
    //   "qx-grid":QxGrid,
    //   PreView,
    },
    props:["dataResource"],
    data() {
      return {
        drawer: false,
        isError:false,
        direction: 'rtl',//right to left
        section:{},
        pdfUrl:""
      };
    },
    mounted(){
        this.$bus.$on("show-attchement-files",(section)=>{
            if(section.documents.length==0){
              return this.$message.error("该课程没有附件资料");
            }
            this.section=section;
            console.log(section);
            let src=`${Interface.online_study.pdf_view}?teacherId=${section.documents[0].teacherId}&filePath=${section.documents[0].path+section.documents[0].name}`;
            this.showAttchementFiles();
            this.checkSource(src);
        });
    },
    beforeDestroy(){
        this.$bus.$off("show-attchement-files");
    },
    computed:{
      gridDataSource(){
        
        let names=this.dataResource.attachementNames.split(",");
        if(this.dataResource.attachementNames=="null"){
          names=[];
        }
        let data=[];
        let res={
          column:6,
          authorId:this.dataResource.authorId+"",
          groupid:this.dataResource.id
        }
        for(let i=0;i<names.length;i++){
          let e = {name:names[i]};
          e.type= function(){
            if(names[i].endsWith(".png") || names[i].endsWith(".jpg")){
              return "img";
            }
            if(names[i].endsWith(".zip")){
              return "zip";
            }
            return "other";
          }();
          data.push(e);
        }
        res.data=data;
        return res;
      }
    },
    methods: {
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(() => {
            done();
          })
          .catch(() => {});
      },
      showAttchementFiles(){
        console.log("显示附件");
        this.drawer = true;
      },
      async checkSource(src){
        try {
          const response = await axios.head(src);
          if (response.status === 200) {
            this.pdfUrl=src;
          } else {
            console.log("Download link is not valid");
          }
        } catch (error) {
          if(error.response.status==500){
            this.isError=true;
            console.error("课件不存在！");
          }
          console.error("An error occurred:", error);
        }
      }
    }
  };
</script>