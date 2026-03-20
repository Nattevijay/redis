package com.redis_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisAppApplication.class, args);
    }

}
