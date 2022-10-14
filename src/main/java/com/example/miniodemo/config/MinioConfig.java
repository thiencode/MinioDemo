package com.example.miniodemo.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinioConfig {
    private final MinioConfigurationProperties minioConfigurationProperties;

    public MinioConfig(MinioConfigurationProperties minioConfigurationProperties) {
        this.minioConfigurationProperties = minioConfigurationProperties;
    }

    @Bean
    @Primary
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                .endpoint(minioConfigurationProperties.getUrl())
                .build();

    }
}
