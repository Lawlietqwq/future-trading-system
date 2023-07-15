package com.csubigdata.futurestradingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@MapperScan("com.csubigdata.futurestradingsystem.dao")
@SpringBootApplication
public class FuturesTradingSystemApplication {

//    public static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(FuturesTradingSystemApplication.class, args);
    }

}
