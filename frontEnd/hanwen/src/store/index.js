import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);
import user from './user.js'
import course_class from './teacher/course-class.js'
import resource from './resource/resource.js'
import notice from './notice/notice.js'
import student_class_room from './student/student-class-room.js'
import members from './teacher/members.js'
import common_data from './common_data.js';

export default new Vuex.Store({
    modules:{
        user,
        course_class,
        resource,
        notice,
        student_class_room,
        members,
        common_data
    }
});