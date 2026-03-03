package com.yws.onlinestudymanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@MapperScan(basePackages = {"com.yws.onlinestudymanage.dao","com.yws.onlinestudymanage.dao.custom"})
@EnableHystrix //该注解将方便客户端启用 Hystrix 断路器，该注解的作用也仅仅是打开断路器
public class OnlineStudyApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OnlineStudyApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(OnlineStudyApplication.class,args);
    }
}
