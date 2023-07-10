package com.example.netdisk.config;

import com.example.netdisk.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig  implements WebMvcConfigurer {


    //主要用于实现无业务逻辑跳转，例如主页跳转，
    //简单的请求重定向，错误页跳转等
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //当访问 “/” 或 “/index.html” 时，都直接跳转到登陆页面
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");

        registry.addViewController("/main.html").setViewName("main");
    }

    //注册拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("MyMvcConfig.addInterceptors 注册拦截器");
       registry.addInterceptor(new LoginInterceptor())
               .addPathPatterns("/**")
               .excludePathPatterns("/", "/login", "/index.html", "/user/login",
                       "/css/**", "/images/**", "/js/**", "/fonts/**");//放行登录页，登陆操作，静态资源
    }
}
