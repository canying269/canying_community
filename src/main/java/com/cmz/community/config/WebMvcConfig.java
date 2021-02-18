package com.cmz.community.config;

import com.cmz.community.controller.interceptor.DataInterceptor;
import com.cmz.community.controller.interceptor.LoginRequiredInterceptor;
import com.cmz.community.controller.interceptor.LoginTicketInterceptor;
import com.cmz.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    //被security替代  登录拦截由security接管 原未登录不能访问的页面
//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");
    }
}
