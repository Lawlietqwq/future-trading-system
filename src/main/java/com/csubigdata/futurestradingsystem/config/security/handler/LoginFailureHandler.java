package com.csubigdata.futurestradingsystem.config.security.handler;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.CustomerAuthenticationException;
import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.util.JsonUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录认证失败处理器
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //设置响应的编码格式
        httpServletResponse.setContentType(Constants.CONTENT_TYPE);
        //获取输出流
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //错误信息
        String msg = null;
        int code = Constants.ERROR;
        //判断异常类型
        if(e instanceof AccountExpiredException){
            msg = "账户过期，登录失败！";
        }else if(e instanceof BadCredentialsException){
            msg = "用户名或密码错误，登录失败！";
        }else if(e instanceof CredentialsExpiredException){
            msg = "密码过期，登录失败！";
        }else if (e instanceof DisabledException){
            msg = "账户被禁用,登录失败！";
        }else if (e instanceof LockedException){
            msg = "账户被锁，登录失败！";
        }else if (e instanceof InternalAuthenticationServiceException){
            msg = "账户不存在，登录失败！";
        }else if (e instanceof CustomerAuthenticationException){
            msg = e.getMessage();
            code = Constants.NO_LOGIN;

        }else{
            msg = "登录失败！";
        }
        //将结果转换成JSON格式
        String result = JsonUtil.objToJson(new Result<>(code, msg));
        //将结果保存在输出流中输出
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

    }
}
