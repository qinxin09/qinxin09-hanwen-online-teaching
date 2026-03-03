<template>
  <div>
    <!-- <el-button @click="homeworkDialogVisible=true">创建作业</el-button> -->
    <!-- <span class="add-class zjgl" style="width:20px;height:20px;" @click="homeworkDialogVisible=true">
        <el-tooltip class="item" effect="dark" content="创建作业" placement="bottom">
        <i class="el-icon-document-add"></i>
        </el-tooltip> 
    </span> -->

    <DialogPublishHomework :publishHomeworkDialogVisible="publishHomeworkDialogVisible" :closePublishHomeworkDialog="closePublishHomeworkDialog" :classrooms="classrooms" :homeworks="getHomeworks()" ></DialogPublishHomework>

    <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
            <span class="add-class zjgl" style="width:20px;height:20px;">
                <i class="el-icon-document"></i>
            </span>
        </span>
    <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="createhomework">创建作业/考试</el-dropdown-item>
        <el-dropdown-item command="publishhomework">发布作业/考试</el-dropdown-item>

    </el-dropdown-menu>
    </el-dropdown>


    <el-dialog ref="ruleForm" :title="dialogTitle" :visible.sync="homeworkDialogVisible" @close="closeDialog('ruleForm')" width="40%"> 
      <div style="padding:0 20px;">
          <el-form :model="form" ref="ruleForm" label-position="left" size="medium" :rules="rules">
            <el-form-item label="标题" prop="title" :label-width="formLabelWidth">
                  <el-input v-model.trim="form.title" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="开始时间" prop="title" :label-width="formLabelWidth">
                <el-col :span="11">
                    <el-form-item prop="startDate">
                        <el-date-picker type="date" placeholder="日期" v-model="form.startDate" style="width: 100%;"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line" :span="2"><div style="display:flex;">-</div></el-col>
                <el-col :span="11">
                    <el-form-item prop="startTime">
                        <el-time-picker placeholder="时间" v-model="form.startTime" style="width: 100%;"></el-time-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="截至时间" prop="title" :label-width="formLabelWidth">
                <el-col :span="11">
                    <el-form-item prop="endDate">
                        <el-date-picker type="date" placeholder="日期" v-model="form.endDate" style="width: 100%;"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line" :span="2"><div style="display:flex;">-</div></el-col>
                <el-col :span="11">
                    <el-form-item prop="endTime">
                        <el-time-picker placeholder="时间" v-model="form.endTime" style="width: 100%;"></el-time-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <!-- 类型 作业或考试-->
            <el-form-item label="类型" prop="title" :label-width="formLabelWidth">
                <el-radio-group v-model="form.type">
                    <el-radio label="zuoye">作业</el-radio>
                    <el-radio label="kaoshi">考试</el-radio>
                    <el-radio label="finalexam">考核</el-radio>
                </el-radio-group>
            </el-form-item>
          </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
      </div>
    </el-dialog>
  
    <el-drawer
        title="创建作业/考试"
        size="80%"
        :visible.sync="homeworkDrawer"
        :direction="direction"
        :before-close="handleClose">
        
        <div style="border:1px solid gray;margin:0 30px;">
            <el-row type="flex" class="my-el-row">
                <el-col :span="2">
                    <div style="display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100%;
                        font-weight:bold;">
                        <span>题型</span>
                    </div>
                </el-col>
                <el-col :span="8">
                    <el-select v-model="topics.type" placeholder="请选择" @change="selectTopicTypeChanged">
                        <el-option
                        v-for="item in topics.options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-col>
            </el-row>

            <el-row type="flex" class="my-el-row">
                <el-col :span="2">
                    <div class="my-label">
                        <span>分值</span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div style="border:1px solid #DCDFE6;border-radius:4px">
                        <div style="margin:10px;">
                            <el-row :gutter="5"  type="flex">
                                <el-col :span="8">
                                    <el-input type="number" v-model="topics.content.score"></el-input>
                                    
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                </el-col>
            </el-row>


            <el-row type="flex" class="my-el-row">
                <el-col :span="2">
                    <div class="my-label">
                        <span>题目</span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <el-input :autosize="{ minRows: 3, maxRows: 6 }" type="textarea" v-model="topics.content.topic" autocomplete="off"></el-input>
                </el-col>
            </el-row>
            <!-- 选项 -->
            <el-row type="flex" class="my-el-row" v-show="topics.type=='danxuan' || topics.type=='duoxuan'">
                <el-col :span="2">
                    <div class="my-label">
                        <span>选项</span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div style="border:1px solid #DCDFE6;border-radius:4px">
                        <div style="margin:10px;">
                            <el-row :gutter="5"  type="flex">
                                <el-col :span="8">
                                    <el-button icon="el-icon-plus" @click="addOptionItem">添加选项</el-button>
                                </el-col>
                                <el-col :span="14">
                                    <el-tag class="my-tag">正确答案:&nbsp;&nbsp;{{ getAnswer() }}</el-tag>
                                </el-col>
                            </el-row>
                        </div>
                        <el-row  type="flex">
                            <el-col :span="24">
                                <div style="margin:0 10px;">
                                    
                                    <div v-for="(options,option_index) in topics.content.options" :key="option_index">
                                        <!-- <el-checkbox :label="getLabel(option_index)" v-model="options.checked" ></el-checkbox>.&nbsp; <el-input type="text" v-model="options.text"></el-input> <el-button icon="el-icon-delete" circle size="mini" type="danger"></el-button> -->
                                        <el-row type="flex" class="options-items">
                                            <el-col :span="2"><el-checkbox @change="checkState(option_index)" :label="getLabel(option_index)" v-model="options.checked" ></el-checkbox>.&nbsp; </el-col>
                                            <el-col :span="16"><el-input type="text" v-model="options.text" placeholder="输入选项内容"></el-input></el-col>
                                            <el-col :span="2">
                                                <div class="option-item-delete">
                                                    <el-button icon="el-icon-delete" circle size="mini" type="danger" @click="deleteOptionItem(option_index)"></el-button>
                                                </div>
                                            </el-col>
                                        </el-row>
                                    </div>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                </el-col>
            </el-row>
            <el-row type="flex" class="my-el-row" v-show="topics.type=='panduan'">
                <el-col :span="2">
                    <div class="my-label">
                        <span>答案</span>
                    </div>
                </el-col>
                <el-col :span="20">
                    <div style="border:1px solid #DCDFE6;border-radius:4px">
                        <div style="margin:10px;">
                            <el-row :gutter="5"  type="flex">
                                <el-col :span="8">
                                    <el-radio-group v-model="topics.content.answer">
                                        <el-radio :label="true">正确</el-radio>
                                        <el-radio :label="false">错误</el-radio>
                                    </el-radio-group>
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 保存按钮 -->
            <el-row type="flex" justify="center">
                <el-col :span="6">
                    <el-tag>累计分数：{{ homework.totalScore }}</el-tag>
                </el-col>
                <el-col :span="12">
                    <!-- <el-button icon="el-icon-check" type="success">保存</el-button> -->
                    <!-- <p>asdf</p> -->
                    <div style="display: flex;justify-content: center;align-items: center;">
                        <el-button icon="el-icon-plus" type="primary" width="20%" @click="addTopic(null)" >添加到作业</el-button>
                        <el-button icon="el-icon-check" type="success" width="20%" @click="saveHomework" >确认创建</el-button>
                    </div>
                </el-col>
                <el-col :span="6"></el-col>
            </el-row>
        </div>
        <!-- 显示作业内容 -->
        <div style="margin:20px;">
            <HomeworkTopics :homeworkTemp="JSON.parse(JSON.stringify(homework))" :viewMode="true" :correctMode="false" :notifyCurrentScore="false" />
        </div>
    </el-drawer>

    
  </div>
</template>

<script>
import uuidv4 from '@/uuid'
import HomeworkTopics from '@/components/classroom/tabs/homework/homeworktopics.vue'
import DialogPublishHomework from '@/components/myteach/teach/dialogPublishHomework.vue'
import { mapActions } from 'vuex'
export default {
    name:"CreateHomeWorkDialog",
    components:{
        HomeworkTopics,DialogPublishHomework
    },
    mounted(){
        setTimeout(()=>{
            // this.homeworkDrawer=true;
        },1000)
    },
    props:["course","classrooms"],
    data(){
        return {
            homeworkDialogVisible:false,
            homeworkDrawer:false,
            publishHomeworkDialogVisible:false,
            form:{
                title:"",
                startDate:"",
                startTime:"",
                endDate:"",
                endTime:"",
                type:""
            },
            rules:{
                title:[
                    { required: true, message: '请输入标题', trigger: 'blur' }
                ],
                startDate: [
                    { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                ],
                startTime: [
                    { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
                ],
                endDate: [
                    { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                ],
                endTime: [
                    { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
                ],
                type: [
                    { required: true, message: '请选择作业类型', trigger: 'change'}
                ]
            },
            topics:{
                type:"",
                options:[
                    {
                        value: 'danxuan',
                        label: '单选题'
                    }, 
                    {
                        value: 'duoxuan',
                        label: '多选题'
                    }, 
                    {
                        value: 'panduan',
                        label: '判断题'
                    }, 
                    {
                        value: 'jianda',
                        label: '简答题'
                    }
                ],
                content:{
                    topic:"",//题目
                    score:0,//分值
                    options:[],//选项
                    userAnswer:[],
                    answer:""//答案
                }
            },
            homework:{
                id:"",
                title:"",
                totalScore:0,
                userAnswer:"_",
                topics:[]
            },
            homeworks:[
                {title:"作业1",id:"",content:"作业内容1",createTime:"2020-01-01 12:00:00",endTime:"2020-01-01 12:00:00"}
            ],
            
            formLabelWidth:"80px",
            direction: 'rtl',//right to left
            dialogTitle:"创建作业/考试"
        }
    },
    methods:{
        ...mapActions("course_class",["teacher_saveHomework"]),
        addOptionItem(){
            if(this.topics.type.length==0){
                return this.$message.warning("请先选择题目类型")
            }

            if(this.topics.type=="danxuan" || this.topics.type=="duoxuan"){
                 this.topics.content.options.push({checked:false,text:"这是选项内容"})
            }else if(this.topics.type=="panduan"){
                
            }
           
        },
        getLabel(index){
            return String.fromCharCode(65+index);
        },
        closeDialog(formName){
            console.log("关闭对话框")
            this.$refs[formName].resetFields();
        },
        submitForm(formName) {
            // console.log("时间：",JSON.parse(JSON.stringify(this.form)));
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // 初始化作业
                    this.homework.id="";
                    this.homework.title=this.form.title;
                    // this.homework.startDate=this.form.startDate;
                    // this.homework.startTime=this.form.startTime;
                    // this.homework.startDateTime=new Date();
                    // this.homework.startDateTime.setFullYear(this.homework.startDate.getFullYear(),this.homework.startDate.getMonth(),this.homework.startDate.getDate());
                    // this.homework.startDateTime.setHours(this.homework.startTime.getHours(),this.homework.startTime.getMinutes(),this.homework.startTime.getSeconds());
                    
                    this.homework.startDateTime=new Date(this.form.startDate);
                    this.homework.startDateTime.setHours(this.form.startTime.getHours(),this.form.startTime.getMinutes(),this.form.startTime.getSeconds());
                    
                    this.homework.endDateTime=new Date(this.form.endDate);
                    this.homework.endDateTime.setHours(this.form.endTime.getHours(),this.form.endTime.getMinutes(),this.form.endTime.getSeconds());
                    this.homework.type=this.form.type;

                    if(this.homework.type.length==0){
                        this.$message.error("请选择作业/考试类型");
                        return;
                    }

                    if(this.homework.startDateTime.getTime()>this.homework.endDateTime.getTime()){
                        this.$message.error("开始时间不能大于结束时间");
                        return;
                    }
                    
                    // 时间间隔不能少于30分钟
                    if(this.homework.endDateTime.getTime()-this.homework.startDateTime.getTime()<30*60*1000){
                        this.$message.error("时间间隔不能少于30分钟");
                        return;
                    }

                    this.homework.totalScore=0;

                    this.homework.topics=[];
                    console.log(this.homework.startDateTime)
                    // 关闭弹窗
                    this.homeworkDialogVisible=false;
                    
                    //打开抽屉
                    this.homeworkDrawer=true;
                }
            });
            
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
            .then(() => {
                done();
            })
            .catch(() => {});
        },
        deleteOptionItem(index){
            this.topics.content.options.splice(index,1);
        },
        getAnswer(){
            let answer="";
            for(let i=0;i<this.topics.content.options.length;i++){
                if(this.topics.content.options[i].checked){
                    answer+=String.fromCharCode(65+i) + ",";
                }
            }
            return answer.slice(0,-1);
        },
        checkState(index){
            //判断题目类型是否可以勾选
            // this.$message.warning(index);
            if(this.topics.type=="danxuan"){
                for(let i=0;i<this.topics.content.options.length;i++){
                    if(i!=index){
                        this.topics.content.options[i].checked=false;
                    }
                }
            }

        },
        selectTopicTypeChanged(value){
            // 选中值发生变化，清空选择的答案
            for(let i=0;i<this.topics.content.options.length;i++){
                this.topics.content.options[i].checked=false;
            }
            if(value=="panduan"){
                this.topics.content.options.splice(0);
            }
        },
    
        addTopic(id){
            // 检查格式
            let topics = JSON.parse(JSON.stringify(this.topics));
            if(topics.type.length==0){
                return this.$message.error("题目类型不能为空");
            }
            if(topics.content.topic.length==0){
                return this.$message.error("题目不能为空");
            }
            if(topics.content.score==0){
                return this.$message.error("分数不能为0");
            }
            if(topics.type === "danxuan"){
                for(let i=0;i<topics.content.options.length;i++){
                    if(topics.content.options[i].checked){
                        topics.content.answer = [i];
                    }
                    delete topics.content.options[i].checked;
                }
                if(topics.content.answer.length==0){
                    return this.$message.warning("请选择单选题正确答案！");
                }
                }else if(topics.type === "duoxuan"){
                    topics.content.answer=[];
                    for(let i=0;i<topics.content.options.length;i++){
                        if(topics.content.options[i].checked){
                            topics.content.answer.push(i);//记录答案位置
                        }
                        delete topics.content.options[i].checked;
                    }
                    if(topics.content.answer.length<1){
                        return this.$message.warning("请选择多选题正确答案！");
                    }
                }else if(topics.type === "panduan"){
                    if(topics.content.answer.length==0){
                        return this.$message.warning("请选择判断题正确答案！");
                    }
                    topics.content.answer=[topics.content.answer];
                }else if(topics.type === "jianda"){
                    topics.content.answer=[];
                }



            console.log(topics);
            // 提取题目
            let topic_={
                id:id || uuidv4(),
                type:topics.type,
                content:topics.content
            }
            // let topic=JSON.parse(JSON.stringify(topic_));
            //存入作业本
            if(id===undefined || id===null){
                console.log("新增")
                this.homework.totalScore=parseInt(this.homework.totalScore*1 + topics.content.score*1);
                this.homework.topics.push(JSON.parse(JSON.stringify(topic_)));
            }
            console.log("题目已添加到作业本",this.homework);
            this.$message.success("添加成功");
            // 重置模板
            this.resetTopics();
        },
        resetTopics(){
            this.topics.type="";
            this.$set(this.topics,"content",{
                    type:"",//类型
                    topic:"",//题目
                    score:0,//分值
                    options:[],//选项
                    userAnswer:[],
                    answer:""//答案
                });
            
        },
        //保存作业
        saveHomework(){
            console.log("保存作业");
            this.homework.id=uuidv4();
            this.homework.courseId=this.course.id;
            //保存到课程
            // 从课程发布到班级

            let course = {
                id:JSON.parse(JSON.stringify(this.course.id))
            }
            
            //提取原来的作业
            let works=JSON.parse(this.course.works+"");
            if(!Array.isArray(works)){
                works=[];
            }
            //插入新作业
            works.push(this.homework);
            course.works=JSON.stringify(works);

            this.teacher_saveHomework({
                data:course,
                callback:{
                    success:(msg)=>{
                        this.$message.success(msg);
                        //关闭抽屉
                        this.homeworkDrawer=false;
                    },
                    error:(msg)=>{
                        this.$message.error(msg);
                    }
                }
            });
        },
        handleCommand(command) {
            if(command === "createhomework"){
                return this.homeworkDialogVisible=true;
            }
            if(command === "publishhomework"){
                console.log("发布作业/考试");
                return this.publishHomeworkDialogVisible=true;
            }
        },
        closePublishHomeworkDialog(){
            return this.publishHomeworkDialogVisible=false;
        },
        getHomeworks(){
            // return this.course==undefined?[]:course.works;
            if(this.course===undefined){
                return [];
            }
            return this.course.works;
        }

    }
}
</script>

<style scoped>
.my-el-row{
    padding:10px;
    margin:10px;
    border-bottom: 1px solid #d4d4d4;
}
.my-label{
    display: flex;
    justify-content: center;
    /* align-items: center; */
    height: 100%;
    font-weight:bold;
}
.options-items {
    display: flex;
    align-items: center;
    margin: 10px 0;
}
.option-item-delete {
    display: none;
    margin-left: 10px;
}
.options-items:hover .option-item-delete{
    display: unset;
}
.my-tag {
    height: 100%;
    align-items: center;
    justify-content: left;
    display: flex;
    width: max-content;
    font-size: 16px;
}
</style>