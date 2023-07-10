package com.example.netdisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetDiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetDiskApplication.class);
        System.out.println("页面的访问地址：http://localhost:8086/hello");
    }

}
