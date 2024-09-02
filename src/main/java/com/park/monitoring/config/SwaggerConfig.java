package com.park.monitoring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class SwaggerConfig implements WebMvcConfigurer {
//
//    @Bean
//    public GroupedOpenApi customOpenApi() {
//        return GroupedOpenApi.builder()
//                .group("custom-api")
//                .pathsToMatch("/log/insert", "/api/regserver")
//                .build();
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .title("CodeArena Swagger")
//                .description("CodeArena 유저 및 인증 , ps, 알림에 관한 REST API")
//                .version("1.0.0");
//    }
//}