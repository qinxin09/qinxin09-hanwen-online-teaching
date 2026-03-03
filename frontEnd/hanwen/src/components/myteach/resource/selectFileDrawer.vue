<template>
  <div>
        <!-- <el-button  icon="el-icon-film"  title="添加视频" @click="dialog = true"><i style="color:green;" class="el-icon-circle-check"></i> 添加视频</el-button> -->
        
        <el-button  icon="el-icon-film"  title="添加视频" @click="selectVideo"><i v-show="videoslength>0" style="color:green;" class="el-icon-circle-check"></i> 添加视频</el-button>
        <el-button icon="el-icon-paperclip" title="添加课件" @click="selectDocument"><i  v-show="documentslength>0" style="color:green;" class="el-icon-circle-check"></i>添加课件</el-button>
        <el-drawer
        title="选择文件!"
        :visible.sync="dialog"
        direction="rtl"
        size="80%"
        :modal="false"
        @open="printMsg">
            <ResourcesList :callbackConfig="true"></ResourcesList>
        </el-drawer>
  </div>
</template>

<script>
import ResourcesList from "@/components/myteach/resource/fileManager/ResourcesList.vue"
export default {
  name:"SelectFileDrawer",
  props:["setFilePosition","chapter_index","section_index","videoslength","documentslength"],
  components:{
    ResourcesList
  },
  data() {
    return {
        resourceType:"",
        dialog: false,
        formLabelWidth: '80px',
        timer: null,
    };
  },
  methods: {
    selectVideo(){
      this.$message.info("selectVideo")
        this.resourceType="video";
        this.dialog = true;

        this.setFilePosition(this.chapter_index,this.section_index,"video");
    },
    selectDocument(){
        this.resourceType="document";
        this.dialog = true;
        this.setFilePosition(this.chapter_index,this.section_index,"document");

    },
    handleClose(done) {
      done();
        // 动画关闭需要一定的时间
        setTimeout(() => {
        }, 400);
    },
    printMsg(){
    }
  },
  mounted(){
    this.$bus.$on("selected-file",()=>{
        this.dialog=false;
        console.log("selected-file ok ,close dialog",this.dialog)
    })
  },
  beforeDestroy(){
    this.$bus.$off("selected-file")
  }
}
</script>

<style>

</style>