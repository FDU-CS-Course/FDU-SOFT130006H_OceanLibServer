package com.oriole.ocean;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.oriole.ocean.dao")
@EnableDubbo
public class OceanNoteServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanNoteServiceApplication.class, args);
    }
}