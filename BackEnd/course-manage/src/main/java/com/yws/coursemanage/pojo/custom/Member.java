package com.yws.coursemanage.pojo.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String name;
    private Integer sex;
    private String phone;
    private String classroomId;
}
