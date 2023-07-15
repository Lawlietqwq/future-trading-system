package com.csubigdata.futurestradingsystem.config.security.handler;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.util.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 匿名用户访问无权限资源的处理器
 */
@Component
public class AnonymousAuthenticationHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //设置响应的编码格式
        httpServletResponse.setContentType(Constants.CONTENT_TYPE);
        //获取输出流
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //将结果转换成JSON格式
        String result = JsonUtil.objToJson(new Result<>(ResultTypeEnum.NO_AUTH));
        //将结果保存在输出流中输出
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
