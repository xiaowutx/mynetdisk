package com.example.netdisk.mapper;

import com.example.netdisk.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLoginMapper {


    User getUserByUsernameAndPassword(User use );

    List<User> getAllUser();

    Integer insertUser(User user);
}
