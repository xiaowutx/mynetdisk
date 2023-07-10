package com.example.netdisk.controller;

import com.example.netdisk.bean.User;
import com.example.netdisk.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserLoginController {

    @Autowired
    UserLoginService userLoginService;

    @PostMapping("/user/login")
    public String doUserLogin(User user, Map<String ,Object> map, HttpSession session){
/*
        if (user!=null&& StringUtils.hasText(user.getUsername())
          && "123456".equals(user.getPassword())) {
            session.setAttribute("loginUser",user);
            System.out.println("LoginController.doLogin 登录成功 用户名："+user.getUsername());
            //防止重复提交，使用重定向
            return "redirect:/main.html";

        }else {
            map.put("msg","用户名或密码错误");
            return  "login";

        }
        */
        //到数据库查询
        User userByUsernameAndPassword = userLoginService.getUserByUsernameAndPassword(user);
        if (userByUsernameAndPassword!=null) {
            session.setAttribute("loginUser",user);
            System.out.println("LoginController.doLogin 登录成功 用户名："+user.getUsername());
            //防止重复提交，使用重定向
            return "redirect:/main.html";

        }else {
            map.put("msg","用户名或密码错误");
            return  "login";

        }


    }
    @RequestMapping("/user/logout")
    public String doLogout(HttpSession session){
        session.removeAttribute("loginUser");
        return "login";
    }
}
