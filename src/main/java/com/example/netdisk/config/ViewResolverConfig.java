package com.example.netdisk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan("com.example.netdisk.controller")
public class ViewResolverConfig implements WebMvcConfigurer {
    //ViewResolver(视图解析器):对于处理器适配器获取到的对象进行解析获取对应的视图最终呈现给浏览器进行渲染（比如jsp页面),ViewResolver组件是根据String类型的视图名和对应的Locale(语言环境 国际化相关)解析出View对象
   @Bean
    public ViewResolver viewResolver(){
       //将JSP文件放在WEB-INF文件夹下，从而避免JSP文件可以通过手动输入的URL被直接访问，只有控制器才能访问它们
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewNames("jsp/*");//当控制器返回的viewName符合规则时才使用这个视图解析器
        viewResolver.setOrder(2);
        return  viewResolver;
    }
}
