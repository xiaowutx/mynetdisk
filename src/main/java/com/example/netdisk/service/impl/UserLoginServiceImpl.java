package com.example.netdisk.service.impl;

import com.example.netdisk.bean.User;
import com.example.netdisk.mapper.UserLoginMapper;
import com.example.netdisk.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    UserLoginMapper mapper;
    @Override
    public User getUserByUsernameAndPassword(User user) {
        return mapper.getUserByUsernameAndPassword(user);
    }
}
