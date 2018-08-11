package com.nino.engineer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.nino.engineer.dao")//配置mybatis包扫描
public class EngineerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngineerApplication.class, args);
    }
}
