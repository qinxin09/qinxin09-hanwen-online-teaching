<template>
    <div style="border:1px solid black;">
        this is index page
        <div>轮播图</div>排行榜
        
    </div>
</template>

<script>
export default {
    name:"index",
    components:{
    },
    mounted(){
        this.$bus.$emit('change-index',0);
    }
}
</script>

<style>

</style>