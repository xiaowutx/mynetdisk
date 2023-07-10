package com.example.netdisk;

import com.example.netdisk.bean.User;
import com.example.netdisk.mapper.UserLoginMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HelloTest {

    @Autowired
    UserLoginMapper mapper;


    @Test
    void getInfo(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        User userByUsernameAndPassword = mapper.getUserByUsernameAndPassword(user);
       System.out.println(userByUsernameAndPassword);
    }

    @Test
    void getAllUser(){
        List<User> allUser = mapper.getAllUser();
        System.out.println(allUser);
    }

    @Test
     void insertUser(){
        User user = new User();
        user.setUsername("王五");
        user.setPassword("654321");
        user.setEmail("123@qq.com");
        Integer integer = mapper.insertUser(user);
        System.out.println(integer);
    }



}
