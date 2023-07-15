package com.csubigdata.futurestradingsystem.config.security;

import com.csubigdata.futurestradingsystem.config.security.filter.CheckTokenFilter;
import com.csubigdata.futurestradingsystem.config.security.handler.*;
import com.csubigdata.futurestradingsystem.config.security.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 注入加密类
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected LoginSuccessHandler loginSuccessHandler;

    @Autowired
    protected LoginFailureHandler loginFailureHandler;

    @Autowired
    protected AnonymousAuthenticationHandler anonymousAuthenticationHandler;

//    @Autowired
//    protected CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Autowired
    protected CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private CheckTokenFilter checkTokenFilter;

    @Autowired
    protected LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 处理登录认证
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录前进行过滤
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.formLogin()          //表单登录
                .loginProcessingUrl("/login") //登录自定义url地址
                .successHandler(loginSuccessHandler)   //认证成功处理器
                .failureHandler(loginFailureHandler)   //认证失败处理器
//                .usernameParameter("username") //默认为username、password
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不创建session
                .and()
                .authorizeRequests()
                .antMatchers("/login?logout").permitAll()
                .antMatchers("/login").permitAll()  //登录请求放行(不拦截)
                .antMatchers("/company").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated() //其他一律请求都需要进行身份认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationHandler)  //匿名无权限访问
//                .accessDeniedHandler(customerAccessDeniedHandler)  //认证用户无权限访问
                .and()
                .cors() //支持跨域请求
                .and()
                .logout()
                .logoutUrl("/logout") //登出自定义url地址
                .logoutSuccessHandler(logoutSuccessHandler);

    }

//    @Override
//    public void configure(WebSecurity web) {
//        //解决静态资源被拦截的问题
//        web.ignoring().antMatchers("/company");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }


}
