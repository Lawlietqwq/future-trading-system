package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.config.redis.RedisService;
import com.csubigdata.futurestradingsystem.entity.User;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.service.UserService;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.util.JwtUtil;
import com.csubigdata.futurestradingsystem.vo.TokenVO;
import com.csubigdata.futurestradingsystem.vo.UserVO;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelService modelService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


//    @PostMapping("/logout")
//    public Result logout(HttpServletRequest request, HttpServletResponse response){
//        //获取token
//        String token = request.getHeader("token");
//        //如果没有从请求参数中获取
//        if (ObjectUtils.isEmpty(token)){
//            token = request.getParameter("token");
//        }
//        //获取用户相关信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null){
//            //清空用户信息
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//            //清空redis里面token
//            String key = "token_" + token;
//            redisService.del(key);
//        }
//        return new Result<>(ResultTypeEnum.LOGOUT_OK);
//    }


    @PostMapping("/register")
    public Result register(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.insertUser(user);
        Result result = new Result(ResultTypeEnum.REGISTER_OK);
        return result;
    }

    @GetMapping("/userInfo")
    public Result<UserVO> getUserInfo(){
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //判断authentication对象是否为空
        if(authentication == null){
            return new Result<>(ResultTypeEnum.QUERY_INFO_FAIL);
        }
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        //创建用户信息对象
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        Result<UserVO> result = new Result<>(ResultTypeEnum.QUERY_INFO_OK);
        result.setData(userVO);
        return result;
    }

    @GetMapping("/company")
    public Result<List<String>> getAllCompany(){
        Result<List<String>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(userService.getAllCompany());
        return result;
    }


    @PostMapping("/refreshToken")
    public Result<TokenVO> refreshToken(HttpServletRequest request){
        //从header中获取前端提交的token
        String token = request.getHeader("token");
        //如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取身份信息
        UserDetails details = (UserDetails) authentication.getPrincipal();
        //重新生成token
        String reToken = "";
        //验证原来的token是否合法
        if (jwtUtil.validateToken(token, details)){
            //生成新的token
            reToken = jwtUtil.refreshToken(token);
        }
        //获取过期时间
        long expireTime = Jwts.parser().setSigningKey(jwtUtil.getSecret())
                .parseClaimsJws(reToken.replace("jwt_",""))
                .getBody().getExpiration().getTime();
        //清除原来的token信息
        String oldTokenKey = "token_" + token;
        //存储新的token
        String newTokenKey = "token_" + reToken;
        //创建TokenVO对象
        TokenVO tokenVO = new TokenVO(expireTime, reToken);
        //返回数据
        Result<TokenVO> result= new Result<>(ResultTypeEnum.RENEW_TOKEN);
        result.setData(tokenVO);
        return result;
    }




}
