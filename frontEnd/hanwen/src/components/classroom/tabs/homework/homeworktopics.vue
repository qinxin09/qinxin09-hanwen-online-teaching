<template>
  <div>
    <div v-for="(topic,index) in homework.topics" :key="topic.key">
        <span style="font-weight:bold;">{{(index+1)+". ["+ transType(topic.type) +"] " }}</span>
        <!-- 题目 -->
        <span>{{ topic.content.topic }}（{{ topic.content.score }}分）</span>
        <span v-if="correctMode">
            得分<input style="width:40px;" type="text" v-model="topic.mark">&nbsp;&nbsp;&nbsp;
        </span>
        <!-- 选项 -->
        <br>
        <!-- 单选题 -->
        <div v-if="topic.type=='danxuan'">
            <div v-for="(option,option_index) in topic.content.options" :key="topic.id+option_index" class="selectItem" @click="setSingleTopicAnswer(topic,option_index)">
                <span :class=" topic.userAnswer && (topic.userAnswer==option_index)?'span selected':'span'">{{ getOptionChar(option_index) }}</span>{{ "  "+ option.text }}
            </div>
            <div style="margin-bottom:10px;"  v-if="correctMode">
                <span style="">学生答案：{{ transAnswer(topic.content.userAnswer,topic.type) }}</span><br>
                <span style="color:green;">正确答案：{{ transAnswer(topic.content.answer,topic.type) }}</span>
            </div>
        </div>
        <!-- 多选题 -->
        <div v-if="topic.type=='duoxuan'">
            <div v-for="(option,option_index) in topic.content.options" :key="topic.id+option_index" class="selectItem" @click="setMultypleTopicAnswer(option,option_index,topic,$event)">
                <!-- <span class="span">{{ getOptionChar(option_index) }}</span>{{ "  "+ option.text }} -->
                <span :class="getMultypleSelectClass(option)">{{ getOptionChar(option_index) }}</span>{{ "  "+ option.text }}
            </div>
            <div style="margin-bottom:10px;"  v-if="correctMode">
                <span style="">学生答案：{{ transAnswer(topic.content.userAnswer,topic.type) }}</span><br>
                <span style="color:green;">正确答案：{{ transAnswer(topic.content.answer,topic.type) }}</span>
            </div>
        </div>
        <!-- 判断题 -->
        <div v-if="topic.type=='panduan'">
            <!-- <div v-for="(option,option_index) in topic.content.options" :key="topic.id+option_index" class="selectItem" @click="setMultypleTopicAnswer(option,option_index)">
                <span :class="option.userAnswer && (option.userAnswer==option_index)?'span selected':'span'">{{ getOptionChar(option_index) }}</span>{{ "  "+ option.text }}
            </div> -->
            <!-- 这里修改需要data里的数据，props修改后不会更新 -->
            <el-radio-group :disabled="correctMode==true" v-model="topic.content.userAnswer" @input="selectTopicMark(topic)">
                <div class="selectItem">
                    <el-radio :label="true">正确</el-radio>
                </div>
                <div class="selectItem">
                    <el-radio :label="false">错误</el-radio>
                </div>
            </el-radio-group>
        </div>
        <!-- 简答题 -->
        <div v-if="topic.type=='jianda'">
            <div style="margin:20px;">
                <el-input :disabled="correctMode==true" :autosize="{ minRows: 3, maxRows: 6 }" type="textarea" v-model="topic.answer" autocomplete="off"></el-input>
            </div>
        </div>
    </div>

    <div v-if="correctMode">
        <div style="margin:20px;">
            <el-input type="textarea" :autosize="{
                minRows: 3,
                maxRows: 6
            }" placeholder="输入评语" v-model="words"></el-input>
        </div>
    </div>
  </div>
</template>

<script>
export default {
    name:"HomeworkTopics",
    props:["homeworkTemp","viewMode","correctMode","notifyCurrentScore"],
    data(){
        return{
            homework:[],
            words:""
        }
    },
    mounted(){
        this.homework=JSON.parse(JSON.stringify(this.homeworkTemp));
        this.$bus.$on("online-send-answer",(callback)=>{
            callback(this.takeHomeworkData());
        })
    },
    beforeDestroy(){
        this.$bus.$off("online-send-answer");
    },
    watch:{
        homeworkTemp:{//创建时需要这个
            deep:true,
            immediate:true,
            handler(){
                this.homework=JSON.parse(JSON.stringify(this.homeworkTemp));
            }
        },
        homework:{
            deep:true,
            immediate:true,
            handler(){
                if(this.correctMode){
                    this.notifyCurrentScore(this.totalMarks());
                }
            }
        }
    },
    methods:{
        totalMarks(){
            let totalCount=0;
            if(this.homework.topics===undefined || this.homework.topics===null){
                return 0;
            }
            for(let i=0;i<this.homework.topics.length;i++){
                if(this.homework.topics[i].mark==null || this.homework.topics[i].mark==undefined){
                    this.homework.topics[i].mark=0;
                }
                totalCount=parseFloat(totalCount*1.0 + this.homework.topics[i].mark*1.0).toFixed(1);
            }
            return totalCount;
        },
        transType(type){
            if(type=="danxuan"){
                return "单选题";
            }
            if(type=="duoxuan"){
                return "多选题";
            }
            if(type=="panduan"){
                return "判断题";
            }
            if(type=="jianda"){
                return "简答题";
            }
            return "其它";
        },
        getOptionChar(index){
            return String.fromCharCode(65+index);
        },
        getMultypleSelectClass(option){
            let result="span";
            if(option.userAnswer!=undefined && option.userAnswer!=null){
                if(option.userAnswer.length>0){
                    result= "span selected"
                }

            }
            
            // console.log("getMultypleSelectClass",result,option);
            return result;
        },
        transAnswer(arr,type){
            if(type=="danxuan" || type=="duoxuan"){
                let answer="";
                for(let i=0;i<arr.length;i++){
                    answer=answer+this.getOptionChar(arr[i]*1);
                }
                return answer;
            }
            if(type=="panduan"){
            }
        },
        setSingleTopicAnswer(topic,option_index){
            if(this.correctMode==true){
                return;
            }
            if(topic.userAnswer==option_index){
                topic.userAnswer="-";
                topic.content.userAnswer=[];//格式化答案
                // return this.$forceUpdate();
            }else{
                topic.userAnswer=option_index+"";//方便标记位置
                topic.content.userAnswer=[option_index+""];//格式化答案
            }
                
            // this.$set(topic,"userAnswer",option_index+"")
            
                //单选题
                topic.mark = 0;
                if(topic.content.userAnswer.length>0){
                    if(topic.content.answer[0]+"" == topic.content.userAnswer[0]+""){
                        topic.mark = parseFloat(topic.content.score*1.0).toFixed(1);
                    }
                }
                console.log(topic.mark,topic.content.userAnswer);
                
            
            return this.$forceUpdate();
        },
        setMultypleTopicAnswer(option,option_index,topic,event){ 
            if(this.correctMode==true){
                return;
            }
            //取消
            let className="span";
            if(option.userAnswer+"" == option_index+""){
                //移除选择的答案
                topic.content.userAnswer.forEach((item,index)=>{
                    if(item==option_index+""){
                        topic.content.userAnswer.splice(index,1);
                        option.userAnswer="";
                        console.log("取消了选择",topic.content.userAnswer)
                    }
                })
            }else{
                //选中
                option.userAnswer=option_index+"";
                topic.content.userAnswer.push(option_index+"");
                className="span selected"
            }
            if(event.target.nodeName=="DIV"){
                event.target.firstChild.setAttribute("class",className);
            }else if(event.target.nodeName=="SPAN"){
                event.target.setAttribute("class",className);
            }
            //多选题设置分数,全部都选对才给分
            topic.mark = 0;
            if((topic.content.userAnswer.length == topic.content.answer.length) && topic.content.userAnswer.length>0){
                // topic.content.userAnswer.sort(
                //         (a,b)=>{
                //             return parseInt(a+"")-parseInt(b+"");
                //         }
                // );
                // topic.content.answer.sort(
                //     (a,b)=>{
                //         return parseInt(a+"")-parseInt(b+"");
                //     }
                // );
                // for(let i=0;i<topic.content.answer.length;i++){
                //     if(topic.content.userAnswer[i]+"" == topic.content.answer[i]+"" ){
                //         topic.mark = topic.content.score;
                //     }
                // }
                topic.mark = topic.content.score;//假设全选对
                for(let i=0;i<topic.content.answer.length;i++){
                    let correct=false
                    for(let j=0;j<topic.content.userAnswer.length;j++){
                        if(topic.content.userAnswer[j]+"" == topic.content.answer[i]+"" ){
                            correct = true;
                        }
                    }
                    // 只要有一个答案不对，就标记为错误
                    if(!correct){
                        topic.mark = 0;
                        break;
                    }
                }
            }
            console.log(topic.mark,topic.content.userAnswer);

            return this.$forceUpdate();
            
        },
        selectTopicMark(topic){
            topic.mark = 0;
            if(topic.content.userAnswer == topic.content.answer[0]){
                topic.mark = topic.content.score;
            }
            console.log(topic.mark,topic.content.userAnswer);
        },
        takeHomeworkData(){
            //获取作业数据
            this.homework.state={
                state:"waiting",
                stateName:"等待批改",
                totalMarks:this.totalMarks(),
                words:""//评语
            }
            if(this.correctMode){
                this.homework.state.state="corrected";
                this.homework.state.stateName="已批改";
                this.homework.state.totalMarks=this.totalMarks(),
                this.homework.state.words=this.words;
            }
            let homework=JSON.parse(JSON.stringify(this.homework))
            console.log("提交的作业",homework);
            
            return homework;
        }
    }
}
</script>

<style scoped>
.selectItem {
    margin:10px;
}
.selectItem .span {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    border: 1px solid #008cff;
    background-color: #ffffff;
    color: rgb(0 0 0);
    text-align: center;
    line-height: 20px;
    margin-right: 5px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    text-align: center;
}
.selectItem .span.selected {
    background-color: #9cd2ff;
}
.selectItem:hover {
    cursor: pointer;
}
</style>