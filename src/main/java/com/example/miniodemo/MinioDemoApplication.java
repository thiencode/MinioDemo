package com.example.miniodemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Media Minio API", version = "1.0", description = "Media Information"))
public class MinioDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinioDemoApplication.class, args);
    }

}
