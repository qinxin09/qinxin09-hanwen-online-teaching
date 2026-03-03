package com.yws.usermanage.service.user.impl;

import com.yws.usermanage.dao.UserMapper;
import com.yws.usermanage.pojo.User;
import com.yws.usermanage.pojo.UserExample;
import com.yws.usermanage.service.user.UserService;
import com.yws.usermanage.utils.MD5Example;
import com.yws.usermanage.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public Result login(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andPhoneEqualTo(user.getPhone());
        //获取并返回用户信息
        List<User> userList=userMapper.selectByExample(userExample);
        String md5Password = MD5Example.MD5hash(user.getPassword());
        if(userList.size()==1){
            if( md5Password.equals(userList.get(0).getPassword())){
                return new Result().success("登录成功",userList.get(0));
            }else{
                return new Result().error("密码错误！",null);
            }
        }else if(userList.size()==0){
            return new Result().error("账号不存在！",null);
        }
//        存在多个相同账号
        return new Result().error("系统错误，存在多个相同账号！",null);
    }

    @Override
    public Result regist(User user) {
        user.setPassword(MD5Example.MD5hash(user.getPassword()));
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andPhoneEqualTo(user.getPhone());
        if(userMapper.selectByExample(userExample).size()==0){
            userMapper.insert(user);
        }else{
            return new Result().error("该账号已被注册！",null);
        }
        return new Result().success("注册成功！",null);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
