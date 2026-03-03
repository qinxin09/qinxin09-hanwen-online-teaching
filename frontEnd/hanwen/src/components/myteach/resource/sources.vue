<template>
  <div>
    <!-- 页头 -->
    <div style="padding:10px;">
      <el-page-header @back="goBack" content="我的资源"></el-page-header>
    </div>
    <!-- 内容 -->
    <div style="padding:10px;">
      <ResourcesList/>
    </div>

  </div>
</template>

<script>
import ResourcesList from '@/components/myteach/resource/fileManager/ResourcesList.vue';
export default {
    name:"Sources",
    components:{
      ResourcesList
    },
    mounted(){
      setTimeout(()=>{
        this.$bus.$emit('slidemenu-index',this.$route.fullPath);
      },500)
    },
    methods:{
      goBack(){
        this.$router.go(-1);
      }
    }
}
</script>

<style>

</style>