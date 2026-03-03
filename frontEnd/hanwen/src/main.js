import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import uuidv4 from './uuid.js';

Vue.use(uuidv4);
Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.config.productionTip = false

Vue.productionTip = false;//

// Vue.config.errorHandler = function (err, vm, info) {
//   // handle error
//   // `info` 是 Vue 特定的错误信息，比如错误所在的生命周期钩子
//   // 只在 2.2.0+ 可用
//   // console.log(err, vm, info);
// }

new Vue({
  el:"#app",
  render: h => h(App), 
  beforeCreate(){
    Vue.prototype.$bus = this;//安装全局事件总线
  },
  router:router,
  store
})
