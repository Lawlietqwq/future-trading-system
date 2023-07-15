package com.csubigdata.futurestradingsystem.config.security.handler;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.config.redis.RedisService;
import com.csubigdata.futurestradingsystem.entity.User;
import com.csubigdata.futurestradingsystem.util.JsonUtil;
import com.csubigdata.futurestradingsystem.util.JwtUtil;
import com.csubigdata.futurestradingsystem.common.LoginResult;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录认证成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //设置响应的编码格式
        httpServletResponse.setContentType(Constants.CONTENT_TYPE);
        //获取输出流
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //获取当前登录用户信息
        User user = (User) authentication.getPrincipal();
        //生成token
        String token = jwtUtil.generateToken(user);
        //设置token签名密钥及过期时间
        long expireTime = Jwts.parser() //获取DefaultJwtParser对象
                .setSigningKey(jwtUtil.getSecret())   //设置签名的密钥
                .parseClaimsJws(token.replace("jwt_", ""))
                .getBody().getExpiration().getTime(); //获取token获取时间
        //创建登录结果对象
        LoginResult loginResult = new LoginResult(user.getUid(),Constants.SUCCESS, token, expireTime);
        //将对象转换成JSON格式
        String result = JsonUtil.objToJson(loginResult);
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        String tokenKey = "token_" + token;
        redisService.set(tokenKey, token, jwtUtil.getExpiration()/1000);
    }
}
