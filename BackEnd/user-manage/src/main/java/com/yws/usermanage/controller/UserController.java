package com.yws.usermanage.controller;

import com.yws.usermanage.pojo.User;
import com.yws.usermanage.service.user.UserService;
import com.yws.usermanage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result loginDo(@RequestBody User user){
        return userService.login(user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/regist")
    public Result registDo(@RequestBody User user){
        return userService.regist(user);
    }
    
}
