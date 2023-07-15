package com.csubigdata.futurestradingsystem.config.security.handler;

import com.csubigdata.futurestradingsystem.common.Constants;

import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.config.redis.RedisService;

import com.csubigdata.futurestradingsystem.util.JsonUtil;
import com.csubigdata.futurestradingsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;


    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //设置响应的编码格式
        httpServletResponse.setContentType(Constants.CONTENT_TYPE);
        //获取输出流
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //获取token
        String token = httpServletRequest.getHeader("token");
        //如果没有从请求参数中获取
        if (ObjectUtils.isEmpty(token)){
            token = httpServletRequest.getParameter("token");
        }
        //获取用户相关信息
        authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null){
            //清空用户信息
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
            //清空redis里面token
            String key = "token_" + token;
            redisService.del(key);
        }
        Result<String> result = new Result<>(ResultTypeEnum.LOGOUT_OK);
        //将对象转换成JSON格式
        String logoutResult = JsonUtil.objToJson(result);
        outputStream.write(logoutResult.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
