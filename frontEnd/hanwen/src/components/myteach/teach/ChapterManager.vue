<template>
  <div loading style="padding:10px;">
    <el-page-header @back="goBack" content="章节管理"></el-page-header>
    
    
    <div class="chapters_content">
      <div>
        <div style="background-color:#f4f4f4">
          <el-row>
          <el-col :span="24">
            <div style="display: inline-flex;justify-content: center;align-items: center;width: 100%;">
              <h4 title="课程名称">课程名称&nbsp;&nbsp;&nbsp;{{ courseName }}</h4>
              <div style="position: absolute; width: 100%;">
                <el-button  style="float: right;margin-right: 20px;" @click="saveChapter">保存</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
        </div>
        
        <!-- 章节编辑区域 -->
        <div style="height:72vh;overflow-y: scroll;border:1px solid gray;">
          <el-empty description="还没有数据！" v-if="chapters.length==0">
            <el-button @click="createChaptersTemplate">创建模板</el-button>
          </el-empty>
          <!-- 循环遍历章节 -->
          <div v-for="(chapter,chapter_index) in chapters" :key="chapter.id" style="border-bottom:1px solid gray;margin-bottom: 20px;padding-bottom:20px;margin:0 10px;">
            <h5 style="display:none;" title="章节名称">{{ chapter.name + chapter.title}}</h5>
            <el-row class="my-el-row">
              <el-col :span="1">
                <span style="font-weight:bold;font-size:17px;">
                  {{ getPreName(chapter_index,false)}}
                </span>
              </el-col>
              <el-col :span="8">
                <div style="display:flex;align-items: center;justify-content: center;">
                  <input style="border:none;border-bottom:1px solid gray;font-weight:bold;font-size:17px;color:gray;width:100%;" type="text" placeholder="请输入章节标题" v-model="chapter.title">
                </div>
              </el-col>
              <el-col :span="13" :push="1">
                <div class="show_btn">
                  <el-button icon="el-icon-plus" title="添加章节" @click="addChapter"></el-button>
                  <el-button icon="el-icon-minus" title="删除章节" @click="delChapter(chapter_index)" type="danger"></el-button>
                </div>
              </el-col>
            </el-row>

            <!-- 循环遍历小结 -->
            <div v-for="(section,section_index) in chapter.sections" :key="section.id">
              <h6 style='display:none;' title="小节名称">{{ section.name+"  "+section.title }}</h6>
              <el-row class="my-el-row">
                <el-col :span="1" :push="1">
                  <span style="font-weight:bold;font-size:14px;">
                    {{ getPreName(section_index,true)}}
                  </span>
                </el-col>
                <el-col :span="7" :push="1" >
                  <div>
                    <input style="border:none;border-bottom:1px solid gray;font-weight:bold;font-size:14px;color:gray;width:100%;" type="text" placeholder="请输入章节标题" v-model="section.title">
                  </div>
                </el-col>
              

                <!-- <el-col :span="12" :push="2" >
                  <div class="show_btn">
                    <div>
                      <el-button icon="el-icon-film"  title="添加视频" @click="1"><i style="color:green;" class="el-icon-circle-check"></i> 添加视频</el-button>
                      <el-button icon="el-icon-paperclip" title="添加课件" @click="1">添加课件</el-button>
                      
                      <el-button icon="el-icon-plus" title="添加小结" @click="addSection(chapter_index)"></el-button>
                      <el-button icon="el-icon-minus"  title="删除小结" @click="delSection(chapter_index,section_index)" type="danger"></el-button>
                    </div>
                  </div>
                </el-col> -->

                <el-col :span="6" :push="2" >
                  <div >
                    <div>
                      <SelectFileDrawer :videoslength="section.videos.length" :documentslength="section.documents.length" :setFilePosition="setFilePosition" :chapter_index="chapter_index" :section_index="section_index"/>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6" :push="2" >
                  <div class="show_btn">
                    <div>
                      <el-button icon="el-icon-plus" title="添加小结" @click="addSection(chapter_index)"></el-button>
                      <el-button icon="el-icon-minus"  title="删除小结" @click="delSection(chapter_index,section_index)" type="danger"></el-button>
                    </div>
                  </div>
                </el-col>
            </el-row>
            <!-- videos -->

            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import uuidv4 from '@/uuid';
import { mapActions, mapState } from 'vuex';
import SelectFileDrawer from '@/components/myteach/resource/selectFileDrawer.vue'
export default {
    name:"ChapterManage",
    components:{
      SelectFileDrawer
    },
    data(){
      return {
        filePosition:{},
        id:"",
        courseName:"",
        chapters:[],
        loading:true,
        
      }
    },
    mounted(){
      // 其它页面里跳转到这个页面时，早已初始化完毕，不会再触发init-course-finish事件，所以手动初始化，即使刷新时会执行两次
      this.initChapterManager();

      //刷新页面时,这个来初始化
      this.$bus.$on("init-course-finish",()=>{
        console.log("$on-->init-course-finish");
        this.initChapterManager();
      });
      this.$bus.$on("selected-file",(fileInfo)=>{
        console.log("$on-->selected-file",fileInfo);
        if(this.filePosition.fileType==="video"){
          if(!fileInfo.name.endsWith(".mp4")){
            return this.$alert("请选择mp4文件！","提示",{type:"warning"});
          }
          this.chapters[this.filePosition.chapterIndex].sections[this.filePosition.sectionIndex].videos.splice(0);
          this.chapters[this.filePosition.chapterIndex].sections[this.filePosition.sectionIndex].videos.push(fileInfo);
        }else if(this.filePosition.fileType==="document"){
          if(!fileInfo.name.endsWith(".pdf")){
            return this.$alert("请选择PDF文件！","提示",{type:"warning"});
          }
          this.chapters[this.filePosition.chapterIndex].sections[this.filePosition.sectionIndex].documents.splice(0);
          this.chapters[this.filePosition.chapterIndex].sections[this.filePosition.sectionIndex].documents.push(fileInfo);
        }
      });
    },
    beforeDestroy(){
      this.$bus.$off("init-course-finish");
      this.$bus.$off("selected-file");
    },
    computed:{
      ...mapState("course_class",["myCreate"])
    },
    methods:{
      setFilePosition(chapterIndex,sectionIndex,fileType){
        this.filePosition={
          chapterIndex,sectionIndex,fileType
        }
      },
      goBack() {
        this.$router.go(-1);
      },
      saveChapter(){
        for(let i=0;i<this.chapters.length;i++){
          if(this.chapters[i].title.length==0){
            return this.$alert("章节名称不能为空");
          }
          let sections = this.chapters[i].sections;
          for(let j=0;j<sections.length;j++){
            if(sections[j].title.length==0){
              return this.$alert("小节名称不能为空");
            }
          }
        }
         console.log(JSON.stringify(this.chapters));
        this.saveChapters({
          data:{
            id:this.id,//课程id
            chapters:JSON.stringify(this.chapters)
          },
          callback:{
            success:(msg)=>{
              this.$message.success(msg);
            },
            error:(err)=>{
              this.$message.error("保存失败",err);
            }
          }
        });
      },
      createChaptersTemplate(){
        console.log("add template")
        this.$set(this._data,"chapters",[{
          id:uuidv4(),
            name:"",title:"",
            sections:[
              {
                id:uuidv4(),
                name:"",
                title:"",
                videos:[],
                works:[],
                documents:[]
              }
            ],
            works:[],
            
          }
        ]);
      },
      ...mapActions("course_class",["getChapterById","saveChapters"]),
      initChapterManager(){
        this.id=this.$route.query.id;
        // console.log("route",this.$route);
        console.log("1 获取课程id",this.id);
        this.getChapterById({id:this.id,
          callback:{
            success:(course)=>{
              let chapters=course.chapters;
              if(chapters==null){
                // 说明没有章节
                this.loading=false;
                this.courseName=course.courseName;
                console.log("chapters is null");
                chapters=[];
              }
              this.$set(this._data,"chapters",chapters);
            },
            error:(err)=>{
              console.error(err);
            }
          }
        })
        
      },
      getPreName(index,isSection){
        return `第${(index+1) + (isSection?'节':'章')}`;
      },
      addChapter(){
        this.chapters.push(JSON.parse(JSON.stringify(this.createTemplate().chapter)));
      },
      delChapter(chapter_index){
        if(this.chapters.length==1){
          this.$message.error("至少保留一个章节");
          return;
        }
        return this.deleteOperation(()=>{this.chapters.splice(chapter_index,1);});
        
      },
      deleteOperation(callback){
        this.$confirm('确认删除吗？, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          callback();
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      },
      addSection(chapter_index){
        console.log(chapter_index,this.chapters[chapter_index])
        this.chapters[chapter_index].sections.push(JSON.parse(JSON.stringify(this.createTemplate().section)));
      },
      delSection(chapter_index,section_index){
        let s = this.chapters[chapter_index].sections;
        if(s.length==1){
          this.$message.error("至少保留一个小节");
          return;
        }
        this.deleteOperation(()=>{s.splice(section_index,1);});
      },
      createTemplate(){
        return {
          chapter:{
            id:uuidv4(),
            name:"",
            title:"",
            sections:[
              {
                id:uuidv4(),
                name:"",
                title:"",
                videos:[],
                works:[],
                documents:[]
              }
            ],
            works:[],
            
          },
          section:{
            id:uuidv4(),
            name:"",
            title:"",
            videos:[],
            works:[],
            documents:[]
          }
        }
      }
    }
}
</script>

<style scoped >
 .my-el-row{
  line-height: 29px;
  height:60px;
  bottom: 11px;
  position: relative;
  display: flex;
  justify-content: left;
  align-items: flex-end;
 }
  
  .my-el-row .show_btn {
    display:none;
  }
  .my-el-row:hover div .show_btn{
    display: block;
    /* background-color: red; */
  }
</style>