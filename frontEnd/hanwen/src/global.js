const HOSTURL="http://localhost";
const Interface={
    user:{
        user_login:`${HOSTURL}:8500/user/login`,
        user_regist:`${HOSTURL}:8500/user/regist`,
    },
    teacher:{
        // create_course:`${HOSTURL}:8200/course/create`,
        // delete_course:`${HOSTURL}:8200/course/delete`,
        // my_create:`${HOSTURL}:8200/course/mycreatecourse`,
        // my_create_classroom:`${HOSTURL}:8200/course/mycreateclassroom`,
        // update_classroom:`${HOSTURL}:8200/course/updateclassroom`,
        // delete_classroom:`${HOSTURL}:8200/course/deleteclassroom`,
        // save_chapters:`${HOSTURL}:8200/course/savechapters`,
        // create_notice:`${HOSTURL}:8200/course/createnotice`,
        // get_notices:`${HOSTURL}:8200/course/getnotices`,
        // get_user_course_notices:`${HOSTURL}:8200/course/getusernotices`,
        // get_teacher_notices:`${HOSTURL}:8200/course/get-teacher-notices`,
        // update_notice:`${HOSTURL}:8200/course/updatenotice`,
        // delete_notice:`${HOSTURL}:8200/course/deletenotice`,
        
        // get_members:`${HOSTURL}:8200/classroom/getmembers`,
        // save_homework:`${HOSTURL}:8200/course/savehomework`,

        // get_classroom:`${HOSTURL}:8200/classroom/get-classroom`,
        // get_classroomworks:`${HOSTURL}:8200/classroom/getclassroomworks`,
        // save_classroom_work:`${HOSTURL}:8200/classroom/setclassroom-work`,
        // clean_classroom_works:`${HOSTURL}:8200/classroom/cleanclassroom-works`,
        // revoke_classroom_work:`${HOSTURL}:8200/classroom/revokeclassroom-work`,


// consumer模块调用
        create_course:`${HOSTURL}:8000/course/create`,
        delete_course:`${HOSTURL}:8000/course/delete`,
        my_create:`${HOSTURL}:8000/course/mycreatecourse`,
        my_create_classroom:`${HOSTURL}:8000/course/mycreateclassroom`,
        update_classroom:`${HOSTURL}:8000/course/updateclassroom`,
        delete_classroom:`${HOSTURL}:8000/course/deleteclassroom`,
        save_chapters:`${HOSTURL}:8000/course/savechapters`,
        create_notice:`${HOSTURL}:8000/course/createnotice`,
        get_notices:`${HOSTURL}:8000/course/getnotices`,
        get_user_course_notices:`${HOSTURL}:8000/course/getusernotices`,
        get_teacher_notices:`${HOSTURL}:8000/course/get-teacher-notices`,
        update_notice:`${HOSTURL}:8000/course/updatenotice`,
        delete_notice:`${HOSTURL}:8000/course/deletenotice`,

        get_members:`${HOSTURL}:8000/classroom/getmembers`,
        save_homework:`${HOSTURL}:8000/course/savehomework`,

        get_classroom:`${HOSTURL}:8000/classroom/get-classroom`,
        get_classroomworks:`${HOSTURL}:8000/classroom/getclassroomworks`,
        save_classroom_work:`${HOSTURL}:8000/classroom/setclassroom-work`,
        clean_classroom_works:`${HOSTURL}:8000/classroom/cleanclassroom-works`,
        revoke_classroom_work:`${HOSTURL}:8000/classroom/revokeclassroom-work`,

    },
    resource:{
        // get_resource:`${HOSTURL}:8300/resource/dir`,
        // create_folder:`${HOSTURL}:8300/resource/create-folder`,
        upload:`${HOSTURL}:8300/resource/chunk-upload`,
        // rename:`${HOSTURL}:8300/resource/rename`,
        // delete:`${HOSTURL}:8300/resource/delete-path`,


        get_resource:`${HOSTURL}:8000/resource/dir`,
        create_folder:`${HOSTURL}:8000/resource/create-folder`,
        // upload:`${HOSTURL}:8000/resource/chunk-upload`,
        rename:`${HOSTURL}:8000/resource/rename`,
        delete:`${HOSTURL}:8000/resource/delete-path`,

    },
    student:{
        // my_class_room:`${HOSTURL}:8400/student/my-class-room`,//学生本人所有的班级信息+课程+章节信息
        // join_class_room:`${HOSTURL}:8400/student/join-class-room`,
        // quit_classroom:`${HOSTURL}:8400/student/quit-classroom`,
        // update_study_record:`${HOSTURL}:8400/student/update-study-record`,
        // get_classroomworks:`${HOSTURL}:8200/classroom/getclassroomworks`


        my_class_room:`${HOSTURL}:8000/student/my-class-room`,//学生本人所有的班级信息+课程+章节信息
        join_class_room:`${HOSTURL}:8000/student/join-class-room`,
        quit_classroom:`${HOSTURL}:8000/student/quit-classroom`,
        update_study_record:`${HOSTURL}:8000/student/update-study-record`,
        get_classroomworks:`${HOSTURL}:8000/classroom/getclassroomworks`
        
    },
    online_study:{
        // video_play:`${HOSTURL}:8400/online-study/video-play`,
        // pdf_view:`${HOSTURL}:8400/online-study/pdf-view`,
        // get_current_course:`${HOSTURL}:8400/online-study/getcurrentcourse`,
        // get_teacher:`${HOSTURL}:8400/online-study/get-teacher`,
        // get_classroom_studyrecord:`${HOSTURL}:8400/classroom/get-classroom-studyrecord`,


        // video_play:`${HOSTURL}:8000/online-study/video-play`,
        // pdf_view:`${HOSTURL}:8000/online-study/pdf-view`,
        video_play:`${HOSTURL}:8500/online-study/video-play`,
        pdf_view:`${HOSTURL}:8500/online-study/pdf-view`,
        get_current_course:`${HOSTURL}:8000/online-study/getcurrentcourse`,
        get_teacher:`${HOSTURL}:8000/online-study/get-teacher`,
        get_classroom_studyrecord:`${HOSTURL}:8000/classroom/get-classroom-studyrecord`,


    }
}

export {Interface};