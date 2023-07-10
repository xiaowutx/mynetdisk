package com.example.netdisk.service;

import com.example.netdisk.bean.User;

public interface UserLoginService {

    User getUserByUsernameAndPassword(User user);
}
