<template>
  <div style="height: 71.9vh;overflow-y: auto;">
    <!-- <pre>
      {{ members }}
    </pre> -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>课程概况</span>
        <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
      </div>
      <div class="text item">
        <el-row>
          <el-col :span="12">
            <div>
              <span>课程名称</span>
              &nbsp;&nbsp;&nbsp;&nbsp;<el-tag type="primary">{{ course.courseName }}</el-tag>
            </div>
          </el-col>
          
          <el-col :span="12">
            <div>
              <span>教师</span>
              &nbsp;&nbsp;&nbsp;&nbsp;<el-tag type="primary">{{ teacher.name }}</el-tag>
            </div>
          </el-col>
        </el-row>
        <br>
        <el-row>
          <el-col :span="24">
            <div style="display:inline-table;">
              <span style="">课程简介</span>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <p style="margin-left: 30px;
    font-size: 13px;">{{ course.courseDesc }}</p>
            </div>
          </el-col>
        </el-row>
        
      </div>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>班级概况</span>
        <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
      </div>
      <div class="text item">
        <el-row>
          <el-col :span="12">
            <div>
              <span>班级名称</span>
              &nbsp;&nbsp;&nbsp;&nbsp;<el-tag type="primary">{{ classroom.name }}</el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div>
              <span>班级人数</span>
              &nbsp;&nbsp;&nbsp;&nbsp;<el-tag type="primary">{{ members.length }}</el-tag>
            </div>
          </el-col>
        </el-row>
        
        <el-divider></el-divider>
        作业/考试
        <el-table
          :data="tableData"
          @expand-change="expandChange"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item :label="'未提交('+props.row.membersNotCommit.length+'人)'">
                  <span v-for="(item,index) in props.row.membersNotCommit" :key="index">
                    <span >{{ item.userName }}</span><el-divider direction="vertical"></el-divider>

                  </span>
                </el-form-item>
                <br>
                <el-form-item label="成绩" class="expands-score">
                  <div v-if="props.row.membersScore.length>0" style="border:1px solid rgb(186, 186, 186);width:500px;padding: 10px;">
                    <el-row class="score-item">
                      <el-col :span="6">用户名</el-col>
                      <el-col :span="6">分数</el-col>
                      <el-col :span="6">用户名</el-col>
                      <el-col :span="6">分数</el-col>
                    </el-row>
                    <!-- <el-row v-for="(item,index) in props.row.membersScore" :key="index" class="score-item">
                      <el-col :span="6">{{ item.userName }}{{ props.row.membersScore[index*2].userName }}</el-col>
                      <el-col :span="6">{{ item.totalMarks }}{{ props.row.membersScore[index*2].totalMarks }}</el-col>
                      <el-col v-if="props.row.membersScore.length>(index*2+1)" :span="6">{{ item.userName }}{{ props.row.membersScore[index*2+1].userName }}</el-col>
                      <el-col v-if="props.row.membersScore.length>(index*2+1)" :span="6">{{ item.totalMarks }}{{ props.row.membersScore[index*2+1].totalMarks }}</el-col>
                    </el-row> -->
                    <!-- <el-row v-for="(index) in (props.row.membersScore.length+1)/2" :key="index" class="score-item">
                      <el-col v-if="props.row.membersScore.length>=(index*2-1)" :span="6">{{ props.row.membersScore[index*2-1].userName }}</el-col>
                      <el-col v-if="props.row.membersScore.length>=(index*2-1)" :span="6">{{ props.row.membersScore[index*2-1].totalMarks }}</el-col>
                      <el-col v-if="props.row.membersScore.length>=(index*2)" :span="6">{{ props.row.membersScore[index*2].userName }}</el-col>
                      <el-col v-if="props.row.membersScore.length>=(index*2)" :span="6">{{ props.row.membersScore[index*2].totalMarks }}</el-col>
                    </el-row> -->

                    <!-- <el-row v-for="(index) in (props.row.membersScore.length+1)/2" :key="index" class="score-item">
                      <el-col v-if="props.row.membersScore.length>(index*2-2)" :span="6">{{ props.row.membersScore[index*2-2].userName }}</el-col>
                      <el-col v-if="props.row.membersScore.length>(index*2-2)" :span="6">{{ props.row.membersScore[index*2-2].totalMarks }}</el-col>
                      <el-col v-if="props.row.membersScore.length>(index*2-1)" :span="6">{{ props.row.membersScore[index*2-1].userName }}</el-col>
                      <el-col v-if="props.row.membersScore.length>(index*2-1)" :span="6">{{ props.row.membersScore[index*2-1].totalMarks }}</el-col>
                    </el-row> -->
                    <el-row v-for="(index) in Math.ceil((props.row.membersScore.length+1)/2)" :key="index" class="score-item">
                      <!-- ceil向上取整 ，偶数时两边都有，奇数时只有左边有，左边一定有，所以不需要判断；只需要判断右边是否存在-->
                      <!-- 左边 -->
                      <el-col :span="6">{{ checkUndefined(props.row.membersScore[index*2-2],"name") }}</el-col>
                      <el-col :span="6">{{ checkUndefined(props.row.membersScore[index*2-2],"marks") }}</el-col>
                      <!-- 右边 -->
                      <el-col v-if="props.row.membersScore.length > index*2-1" :span="6">{{ checkUndefined(props.row.membersScore[index*2-1],"name") }}</el-col>
                      <el-col v-if="props.row.membersScore.length > index*2-1" :span="6">{{ checkUndefined(props.row.membersScore[index*2-1],"marks") }}</el-col>

                    </el-row>
                  </div>
                  <span v-else>暂无数据</span>
                </el-form-item>
                  

              </el-form>
              
              <!-- <el-row>
                <el-col :span="24">
                  成绩：{{  }}
                </el-col>
              </el-row> -->

              <el-row>
                <el-col :span="12">
                  <!-- <span>分数段饼图</span> -->
                  <div :ref="props.row.workId" style="width: 300px; height: 200px;"></div>
                </el-col>
                <el-col :span="12">
                  <!-- <span>柱形图</span> -->
                  <div :ref="props.row.workId+'bar'" style="width: 500px; height: 300px;"></div>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
          <el-table-column
            label="名称"
            prop="workTitle">
          </el-table-column>
          <el-table-column
            label="总分"
            prop="totalScore">
          </el-table-column>
          <el-table-column
            label="平均分"
            prop="averageScore">
          </el-table-column>
        </el-table>
        
      </div>
    </el-card>
        
        <div id="chart2" style="width: 300px; height: 200px;"></div>
  </div>
</template>

<script>
import * as echarts from "@/echarts/echarts.min.js";
import "@/echarts/echarts.simple.min.js";
import { mapActions, mapGetters, mapState } from 'vuex';
export default {
    name:"DataStatistics",
    data(){
      return {
        init_state:{},
        tableData:[
          // {
          //   workTitle:"第一次作业",
          //   workId:"asfasf-sdfsaf--sdffsafds",
          //   averageScore:100,
          //   totalScore:100,
          //   members:[
          //     {
          //       name:"张三",
          //       score:100
          //     },
          //     {
          //       name:"李四",
          //       score:90
          //     }
          //   ]
          // }
        ]
      }
    },
    watch:{
      init_state(v){
        let keys = Object.keys(v);
        console.log("inited",keys);
        if(keys.length==6){
          this.tableData.splice(0);
          //六项数据已加载完毕，开始计算表的数据
          //遍历发布到班级里的所有作业
          for(let i=0;i<this.classroom_works.length;i++){
            let item={};
            let averageScore=0;
            // 遍历每个成员的学习记录，获取到对应的作业,解析成绩、workname、state
            let membersScore=[];
            let membersNotCommit = [];//没有提交该作业的成员
            let workTitle="";
            let totalScore=0;
            let totalMarks=0;
            let state="";
            for(let j=0;j<this.classroom_studyrecord.length;j++){
              // console.log(this.classroom_studyrecord[j],this.classroom_studyrecord[j].studyRecord)
              let works = null;
              if(this.classroom_studyrecord[j].studyRecord==null || this.classroom_studyrecord[j].studyRecord==undefined){
                this.classroom_studyrecord[j].studyRecord="";
              }
              if(this.classroom_studyrecord[j].studyRecord.length>0){
                works= JSON.parse(this.classroom_studyrecord[j].studyRecord).works_done;
              }
              
              //处理没有的学习记录
              if(works==undefined || works==null){
                // 没有交作业的
                membersNotCommit.push({
                  userName:this.getUserNameById(this.classroom_studyrecord[j].userId),
                  userId:this.classroom_studyrecord[j].userId,
                })
                continue;//跳过本次剩下的代码
              }
              // console.log("works",works);
              // 遍历每个成员的学习记录里的每个作业,拿到指定作业
              let work=null;
              for(let k=0;k<works.length;k++){
                if(works[k].id==this.classroom_works[i].workId){
                  work=works[k];
                  break;
                }
              }
              if(work!=null){
                // 提取这个作业的相关数据
                workTitle=work.title;
                totalScore=work.totalScore;
                state =work.state.state;
                if(work.state.state=="corrected"){
                  totalMarks=work.state.totalMarks;
                  averageScore=parseFloat(averageScore)+parseFloat(totalMarks);
                }
                membersScore.push({
                  userName:this.getUserNameById(this.classroom_studyrecord[j].userId),
                  userId:this.classroom_studyrecord[j].userId,
                  totalMarks,
                  state
                })
              }else{
                membersNotCommit.push({
                  userName:this.getUserNameById(this.classroom_studyrecord[j].userId),
                  userId:this.classroom_studyrecord[j].userId,
                  totalMarks:0,
                  state:"notSubmitted"
                })
              }
            }
            //考虑可能都没有提交作业的情况，需要从course.works里获取作业信息
            let works=this.course.works;
            if(works!=undefined && works!=null){
              works=JSON.parse(this.course.works);
              for(let j=0;j<works.length;j++){
                if(works[j].id==this.classroom_works[i].workId){
                  workTitle=works[j].title;
                  totalScore=works[j].totalScore;
                }
              }
            }
            console.log(totalScore,membersScore.length)
            if(averageScore>0 && membersScore.length>0){
              averageScore=parseFloat(averageScore/membersScore.length)
            }
            
            this.tableData.push({
              workId:this.classroom_works[i].workId,
              workTitle:workTitle,
              totalScore,
              averageScore:averageScore,
              membersNotCommit,
              membersScore,
            })

          }
          // 解析完毕：
          console.log(this.tableData)
        }
      }
    },
    mounted(){
      // this.createPie2();
      this.initData();
    },
    computed:{
      ...mapState("common_data",["members","classroom_studyrecord","course","teacher","classroom","classroom_works"]),
      ...mapGetters("common_data",["chapters","getUserNameById"])
    },
    beforeDestroy(){
      this.$off("tabs-change");
    },
    methods:{
      ...mapActions("common_data",["initMembers","initClassroomAllRecord","ininCourse","initTeacher","initClassroom","initClassroomWorks"]),
      checkUndefined(obj,type){
        if(type=="name"){
          return obj === undefined ? '' : obj.userName ;
        }
        else if(type=="marks"){
          return obj === undefined ? '' : obj.totalMarks ;
        }
        return "";
        
      },
      expandChange(row,expandedRows){
        console.log(row.workId)
        setTimeout(()=>{
          console.log(this.$refs[row.workId]);

          try{
            const data = [
                {
                  name:"<60",
                  value:function(){
                      return row.membersScore.filter(element => {
                        return parseInt(element.totalMarks)<60;
                      });
                    }().length
                },
                {
                  name:"60~70",
                  value:function(){
                      return row.membersScore.filter(element => {
                        return parseInt(element.totalMarks)>=60 && parseInt(element.totalMarks)<70;
                      });
                    }().length
                },
                {
                  name:"70~90",
                  value:function(){
                      return row.membersScore.filter(element => {
                        return parseInt(element.totalMarks)>=70 && parseInt(element.totalMarks)<90;
                      });
                    }().length
                },
                {
                  name:"90~100",
                  value:function(){
                      return row.membersScore.filter(element => {
                        return parseInt(element.totalMarks)>=90 && parseInt(element.totalMarks)<=100;
                      });
                    }().length
                }
              ];
              console.log(data)
            this.createPie(this.$refs[row.workId],
            data);
            this.createBar(this.$refs[row.workId+"bar"],{
              xData:row.membersScore.map(item => item.userName),
              yData:row.membersScore.map(item => item.totalMarks)
            })
          }catch(e){
            console.error(e);
          }
        },500)
        console.log(expandedRows);
      },
      initData(){
        // 点击切换到本标签页时开始准备数据
        this.$bus.$on("tabs-change",(name)=>{
          if(name=="datastatistics"){
            console.log("点击了 datastatistics");
            // 获取班级成员
            this.initMembers({
              data:{
                classroomId:this.$route.query.classroomId
              },
              callback:{
                success:(msg)=>{
                  this.$set(this.init_state,"members",true)
                },
                error:(msg)=>{
                  // this.$message.error(msg);
                }
              }
            });
            // 获取班级学习记录
            this.initClassroomAllRecord({
              data:{
                classroomId:this.$route.query.classroomId
              },
              callback:{
                success:(msg)=>{
                  this.$set(this.init_state,"classroomAllRecord",true)
                }
              }
            })
            // 获取当前班级对应的课程
            this.ininCourse({
              data:{
                courseId:this.$route.query.courseId
              },
              callback:{
                success:(msg,teacherId)=>{
                  this.$set(this.init_state,"course",true)
                  this.initTeacher({
                    data:{teacherId:parseInt(teacherId)},
                    callback:{
                      success:()=>{
                        this.$set(this.init_state,"teacher",true)
                      },
                      error:()=>{}
                    }
                  })
                },
                error:()=>{}
              }
            });
            this.initClassroom({
              data:{
                classroomId:this.$route.query.classroomId
              },
              callback:{
                success:()=>{
                  this.$set(this.init_state,"classroom",true)
                },
                error:()=>{}
              }
            })
            this.initClassroomWorks({
              data:{
                classroomId:this.$route.query.classroomId
              },
              callback:{
                success:()=>{
                  this.$set(this.init_state,"classroomWorks",true);
                },
                error:()=>{}
              }
            })
            
          
          }
        })
      },
      
      //创建饼图
      createPie(container,myData){
        // 初始化ECharts实例
        let myChart = echarts.init(container);
        // 配置项
        var option = {
          title: {
            text: '分数段',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} 人({d}%)'
            /**
             * trigger: 'item' 表示触发工具提示的方式，此处设置为当鼠标悬停在数据项上时触发。
formatter: '{a} <br/>{b}: {c} ({d}%)' 表示工具提示的格式化字符串，用于显示工具提示的内容。其中，{a}代表系列名称，{b}代表数据项名称，{c}代表数据值，{d}代表百分比。<br/>用于换行。
举个例子解释一下，假设有一个饼图，其中有一个数据项的名称为“苹果”，数据值为20，百分比为25%，那么工具提示显示的内容就是： 苹果 数据项名称: 20 (25%) 其中，“数据项名称”可能是某个具体的数据项类型，例如“销售额”、“数量”等。

这样设置工具提示后，当鼠标悬停在饼图的数据项上时，会以指定的格式显示相应的数据信息。
             */
          },
          series: [
            {
              name: '分数段',
              type: 'pie',
              radius: '50%',
              // data: [
              //   {value: 335, name: '直接访问'},
              //   {value: 310, name: '邮件营销'},
              //   {value: 234, name: '联盟广告'},
              //   {value: 135, name: '视频广告'},
              //   {value: 1548, name: '搜索引擎'}
              // ],
              data:myData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        myChart.setOption(option);
      },
      createPie2(){
        let data = [
          { name: '张三', score: 80 },
          { name: '李四', score: 90 },
          { name: '王五', score: 70 },
          // 其他成员的成绩数据...
        ];
        let option = {
          title: {
            text: '班级成员成绩分布', // 图表标题
            textStyle: {
              fontSize: 16,
              fontWeight: 'bold',
            },
            left: 'center', // 标题居中对齐
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
          },
          series: [
            {
              name: '成绩',
              type: 'pie',
              radius: '70%', // 饼图半径，可以设置为百分比或具体的数值
              data: data.map(item => ({ name: item.name, value: item.score })),
              // 饼图数据，每个数据项包含一个成员姓名和对应的成绩值
            },
          ],
        };
        let myChart = echarts.init(document.getElementById('chart2'));
        myChart.setOption(option);
      },
      // 柱形图
      createBar(container,data){
        // 初始化echarts实例
        const myChart = echarts.init(container);

        /*
        // 成绩数据
        const data = [
          { name: '张三', score: 85 },
          { name: '李四', score: 90 },
          { name: '王五', score: 78 },
          { name: '赵六', score: 92 },
          { name: '钱七', score: 88 },
        ];

        // x轴数据：成员姓名
        const xData = data.map(item => item.name);

        // y轴数据：成绩
        const yData = data.map(item => item.score);

        */
       const xData=data.xData;
       const yData=data.yData;
        // 配置项
        const option = {
          title:{
            text:"成绩统计",
          },
          xAxis: {
            type: 'category',
            data: xData,
          },
          tooltip: {
            trigger: 'axis', // 使用轴触发类型
            formatter: '{b}: {c}', // 设置提示框样式和显示内容
            /**
             * trigger 属性设置为 'axis' 表示使用轴触发类型，当鼠标悬停在图表的柱状上时触发显示提示信息。
formatter 属性设置为 '{b}: {c}' 表示显示的提示信息的样式，{b} 表示类目名称，{c} 表示数据值。
             */
          },
          yAxis: {
            type: 'value',
          },
          series: [{
            type: 'bar',
            data: yData,
          }],
        };

        // 使用配置项生成柱状图
        myChart.setOption(option);
      }
  }
}
</script>

<style scoped>
.clearfix{
  border-left: 3px solid #5096f5;
  padding-left:3px;
}
.box-card {
  margin-bottom:20px;
}
.expands-score {
  width:100% !important;
}
.score-item {
  border-bottom: 1px solid #ccc;
}
.score-item:last-child {
  border-bottom:none;
}
</style>