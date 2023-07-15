package com.csubigdata.futurestradingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;

@SpringBootTest
class FuturesTradingSystemApplicationTests {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void test1(){
        System.out.println(passwordEncoder.encode("wanquan"));
    }


    @Test
    public void test2(){
        System.out.println("$2a$2a$10$5WG2/cKy0z6tmiT1hfr5G.OV/NO5ffpRprWK7c9BC/fhCB9DMEH0a".length());
        System.out.println("$2a$10$NnPcOOrph7nRXa3roXKexeQi824aVpB0WTOjB1sePTO4vE9nl56SK".length());
    }
}
