package com.internship.internship.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfiguration {

    @Value("${application-email}")
    private String email;
    @Value("${application-url}")
    private String url;
    @Value("${application-author}")
    private String author;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenApi(@Value("${application-description}") String appDescription,
                                 @Value("${application-version}") String appVersion) {
        return new OpenAPI().info(new Info().title("Application API")
                .version(appVersion)
                .description(appDescription)
                .title("Task Tracker")
                .license(new License().name("Apache 3.0")
                        .url("http://springdoc.org"))
                .contact(
                        new Contact()
                                .email(email)
                                .url(url)
                                .name(author)));
    }
}