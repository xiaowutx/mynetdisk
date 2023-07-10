package com.example.netdisk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HelloThController {

    @GetMapping("/toHello")
    public String hello(Map<String,Object> map){
        map.put("name","贵州理工学院");
        return  "hello";
    }

    @GetMapping("/helloJSP")
    public String helloJSP(){
        return "jsp/helloTest";
    }
}
