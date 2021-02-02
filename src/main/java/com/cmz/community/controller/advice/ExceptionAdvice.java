package com.cmz.community.controller.advice;

import com.cmz.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常：" + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        //判断请求方式
        String xRequestedWith = request.getHeader("x-requested-with");
        //异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            //响应字符串 /plain普通字符串可以是json格式需手动转换 /json json字符串
            response.setContentType("application/plain;charSet=utf-8");
            PrintWriter writer = response.getWriter();

            writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else{
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }


}
