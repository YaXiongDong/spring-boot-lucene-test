package com.pxkj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = { "com.pxkj.dao" })
@EnableTransactionManagement
public class SpringBootLuceneTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLuceneTestApplication.class, args);
    }
}
