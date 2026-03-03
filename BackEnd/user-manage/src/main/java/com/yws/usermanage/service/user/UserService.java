package com.yws.usermanage.service.user;

import com.yws.usermanage.pojo.User;
import com.yws.usermanage.utils.Result;

public interface UserService {
    Result login(User user);
    Result regist(User user);
    User getUserById(Long id);
}
