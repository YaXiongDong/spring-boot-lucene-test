package com.pxkj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = { "com.pxkj.dao" })
public class SpringBootLuceneTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLuceneTestApplication.class, args);
    }
}
