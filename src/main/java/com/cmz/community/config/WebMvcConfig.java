package com.cmz.community.config;

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

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/*","/js/*","/img/*");
    }
}
