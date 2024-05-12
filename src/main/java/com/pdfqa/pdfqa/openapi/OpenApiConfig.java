package com.pdfqa.pdfqa.openapi;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springboot")
                .packagesToScan("com.pdfqa.pdfqa")
                .build();
    }
}
