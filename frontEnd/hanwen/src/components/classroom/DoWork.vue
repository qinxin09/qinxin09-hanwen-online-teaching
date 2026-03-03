<template>
  <div>
    <el-button-group>
        <el-button type="primary" :disabled="theWorksIsDone" @click="todoWork()">{{ theWorksIsDone?"已完成":"去完成"}}</el-button>
        <el-button type="success" :disabled="!theWorksIsDone" @click="queryScore(homeworkTemp,studyRecord)">查成绩</el-button>
    </el-button-group>
    <el-drawer
    title="作业内容"
    :visible.sync="showDoWorkDrawer"
    size="100%"
    @open="openDoWorkDrawer"
    :with-header="false">
        <!-- 显示作业内容 -->
        <div>
            <el-row type="flex" style="text-align: center;align-items: center;margin-top: 10px;">
                <el-col :span="6">
                    <span>{{ homeworkTemp.title }}{{"    总分："+ homeworkTemp.totalScore }}</span>
                </el-col>
                <el-col :span="12">
                    <span v-if="homeworkTemp.type=='kaoshi'">倒计时{{ countDown }}</span>
                </el-col>
                <el-col :span="6">
                    <el-button type="success" @click="sendAnswer(false)">提交</el-button>
                    <el-button type="danger" @click="showDoWorkDrawer=false">关闭</el-button>
                </el-col>
            </el-row>
        </div>
        <hr>
        <div style="overflow-y: auto;">
            <div style="margin:20px 70px;height:83vh;">
                <HomeworkTopics :homeworkTemp="homeworkTemp" :viewMode="true" />
            </div>
        </div>
    </el-drawer>
  </div>
</template>

<script>
import HomeworkTopics from '@/components/classroom/tabs/homework/homeworktopics.vue'
import { mapActions, mapGetters, mapState } from 'vuex';
export default {
    name:"DoWork",//作业管理
    props:["homeworkTemp","queryScore"],
    components:{
        HomeworkTopics
    },
    data(){
        return {
            showDoWorkDrawer:false,
            //倒计时
            countDown:0,
            timer:null,
            studyRecord:{
                works_done:[]
            },
            recordId:""
        }
    },
    mounted(){
        // this.timer = setInterval(()=>{
        //     let endTime = new Date(this.homeworkTemp.endDateTime);
        //     let seconds = (endTime.getTime() - new Date().getTime())/1000;
        //     if(seconds<0){
        //         this.sendAnswer();
        //         clearInterval(this.timer);
        //     }
        //     // "HH:MM:SS"
        //     this.countDown = ?;
        //     console.log(this.countDown);
        // },1000);
        this.initstudyRecord();
        // this.$bus.$on("tabs-change",(name)=>{
        //     if(name=="homeworktab"){
        //     console.log("点击了homework,=============")
        //     this.initstudyRecord();
        //     }
        // })
    },
    computed:{
        ...mapState("user",["user"]),
        ...mapGetters("student_class_room",["getClassroomById"]),
        theWorksIsDone(){
            return this.checkWorksDone();
        }
    },
    methods:{
        checkWorksDone(){
            let homework=this.homeworkTemp;
            // console.log(this.studyRecord)
            if(this.studyRecord.works_done.length>0){
                for(let i=0;i<this.studyRecord.works_done.length;i++){
                    if(this.studyRecord.works_done[i].id==homework.id){
                        return true;
                    }
                }
            }
            return false;
        },
        initstudyRecord(){
            //获取学习记录
            let classroom=this.getClassroomById(this.$route.query.courseId);
            console.log("DoWork.vue init 学习记录： ",classroom);
            if(classroom === null){
                console.error("获取学习记录失败!");//加入班级的时候就会创建学习记录
                return this.$alert("系统异常，获取数据失败");
            }
            this.recordId = classroom.recordId;
            let studyRecord = classroom.studyRecord || "";
            //解析学习记录
            if(studyRecord.length>0){
                this.studyRecord = JSON.parse(studyRecord);
            }else{
                this.studyRecord={
                    works_done:[]
                };
            }
            if(this.studyRecord.works_done===undefined || this.studyRecord.works_done.length===null){
                this.studyRecord.works_done=[];
            }
            // console.log("提交前的数据",this.studyRecord);
        },
        ...mapActions("student_class_room",["updateStudyRecord"]),
        openDoWorkDrawer(){
            //判断是否为考试
            console.log(this.homeworkTemp.type)
            if(this.homeworkTemp.type!="kaoshi"){
                return console.log("非考试模式");
            }else{
                console.log("考试模式",this.homeworkTemp.type=="kaoshi")
                const padZero = (num) => {
                    return num < 10 ? "0" + num : num;
                };

                this.timer = setInterval(() => {
                    let endTime = new Date(this.homeworkTemp.endDateTime);
                    let seconds = (endTime.getTime() - new Date().getTime()) / 1000;
                    if (seconds < 0) {
                        this.sendAnswer(true);
                        clearInterval(this.timer);
                        this.$alert("时间到，已自动交卷！");
                    }
                    let hours = Math.floor(seconds / 3600);
                    seconds = seconds % 3600;
                    let minutes = Math.floor(seconds / 60);
                    let remainingSeconds = Math.floor(seconds % 60);
                    this.countDown = padZero(hours) + ':' + padZero(minutes) + ':' + padZero(remainingSeconds);
                    // console.log(this.countDown);
                }, 1000);
            }
            
        },

        sendAnswer(automode){
            

            let callback = (homework)=>{
                console.log("这是提交的作业");
                console.log(homework);
                if(homework.topics.length==0){
                    console.error(homework);
                    return this.$alert("出错了！")
                }
                // 手动提交
                if(!automode){
                        //判断是否都做完了
                        let undoList=[];
                    for(let i=0;i<homework.topics.length;i++){
                        if(homework.topics[i].type!=="jianda"){
                            if(homework.topics[i].content.userAnswer.length==0){
                                undoList.push((i+1)+". "+homework.topics[i].content.topic);
                            }
                        }else{
                            // console.log(homework.topics[i].hasOwnProperty("answer"))
                            if(homework.topics[i].hasOwnProperty("answer") && homework.topics[i].answer.length>0){
                            }else{
                                undoList.push((i+1)+". "+homework.topics[i].content.topic);
                            }
                        }
                    }
                    // console.log(undoList)
                    if(undoList.length>0){
                        return this.$alert("还有未完成的题目，请检查！<br>"+undoList.join("<br>"),"未完成题目",{
                            confirmButtonText:"确定",
                            dangerouslyUseHTMLString: true,
                            callback:()=>{
                                console.log("点击确定");
                            }
                        });
                    }
                }
                
                // return this.$message.success("testmode");
                //合并学习记录
                this.studyRecord.works_done.push(homework);
                console.log(this.studyRecord);
                //保存学习记录
                // return xxxx;
                this.updateStudyRecord({
                    data:{
                    recordId:this.recordId,
                    studyRecord:JSON.stringify(this.studyRecord)},
                    callback:{
                    success:(msg)=>{
                        this.showDoWorkDrawer=false;
                        this.$message.success({
                            message:msg,
                            duration:2000
                        });
                        this.$forceUpdate();
                    },
                    error:(msg)=>{this.$message.error(msg)}
                    }
                });
            }
            this.$bus.$emit("online-send-answer",callback);//
        },
        todoWork(){
            if(this.homeworkTemp.type=="kaoshi"){
                if(new Date(this.homeworkTemp.startDateTime).getTime() >= new Date().getTime()){
                    this.$alert("考试开始时间未到!");
                    return;
                }else if(new Date(this.homeworkTemp.endDateTime).getTime() <= new Date().getTime()){
                    this.$alert("考试已结束!");
                    return;
                }
            
                //确认开始考试
                this.$confirm('确认开始考试吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                    }).then(() => {
                        this.showDoWorkDrawer=true;
                    }).catch((e) => {console.log(e)});
            }else{
                this.showDoWorkDrawer=true;
            }
        }
    }
}
</script>

<style>

</style>