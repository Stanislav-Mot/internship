package com.internship.internship.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
@Data
public class AppProperties {
    private Boolean cache;
    private String version;
    private String description;
    private String url;
    private String email;
    private String author;
}
