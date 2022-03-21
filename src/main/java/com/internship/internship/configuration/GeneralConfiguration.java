package com.internship.internship.configuration;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Group;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GeneralConfiguration {

    @Value("${custom.email}")
    private String email;
    @Value("${custom.url}")
    private String url;
    @Value("${custom.author}")
    private String author;

    @Bean

    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<Group, GroupDto> converter = context -> {

            GroupDto dto = new GroupDto();
            List<Assignment> children = context.getSource().getChildren().stream()
                    .map(Assignment.class::cast).collect(Collectors.toList());

            List<Assignment> tasks = context.getSource().getTasks().stream()
                    .map(Assignment.class::cast).collect(Collectors.toList());

            List<Assignment> collect = new ArrayList<>();
            collect.addAll(children);
            collect.addAll(tasks);
            dto.setAssignments(collect);
            dto.setName(context.getSource().getName());
            dto.setId(context.getSource().getId());
            return dto;
        };
        modelMapper.createTypeMap(Group.class, GroupDto.class).setConverter(converter);
        modelMapper.getConfiguration().setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
        return modelMapper;
    }

    @Bean
    public OpenAPI customOpenApi(@Value("${custom.description}") String appDescription,
                                 @Value("${custom.version}") String appVersion) {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                )
                .info(new Info().title("Application API")
                        .version(appVersion)
                        .description(appDescription)
                        .title("Task Tracker")
                        .license(new License().name("Apache 3.0")
                                .url("https://springdoc.org"))
                        .contact(
                                new Contact()
                                        .email(email)
                                        .url(url)
                                        .name(author)));
    }
}