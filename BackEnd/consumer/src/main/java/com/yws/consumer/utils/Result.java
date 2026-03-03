package com.yws.consumer.utils;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private String msg;
    private Object data;
    private int code;

    public Result success(String msg, Object data){
        this.msg=msg;
        this.data = data;
        this.code=200;
        return this;
    }
    public Result error(String msg, Object data){
        this.msg=msg;
        this.data = data;
        this.code=400;
        return this;
    }
}
