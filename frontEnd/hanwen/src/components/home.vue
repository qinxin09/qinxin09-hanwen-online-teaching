<template>
    <div style="height:100vh;">
        <div style="height:12vh;">
            <NavBar></NavBar>
        </div>
        <div style="height:88vh;">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import { mapMutations, mapState } from 'vuex';
import NavBar from './NavBar.vue';
export default {
    name:"home",
    components:{
        NavBar
    },
    computed:{
        ...mapState("user",["isLogin","user"])
    },
    methods:{
    },
    mounted(){
        if(!this.isLogin){
            this.$router.push('/login');
        }
        console.log(this.user);
        if(this.$route.fullPath=="/"){
            if(this.user.role==1){
                this.$router.push('/my_teach/course_class');
            }else{
                this.$router.push('/my_course/stu_course_class');
            }
            
        }
    },
    methods:{
        
    }
}
</script>

<style>

</style>