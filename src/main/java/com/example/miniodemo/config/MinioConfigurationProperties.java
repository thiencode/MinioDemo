package com.example.miniodemo.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
@ToString
public class MinioConfigurationProperties {

    @JsonProperty("access-key")
    private String accessKey;

    @JsonProperty("secret-key")
    private String secretKey;

    @JsonProperty("url")
    private String url;

    @JsonProperty("bucket")
    private String bucket;

}