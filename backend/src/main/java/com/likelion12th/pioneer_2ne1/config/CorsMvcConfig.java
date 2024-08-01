package com.likelion12th.pioneer_2ne1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";


    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
//                .exposedHeaders("Set-Cookie")

                .exposedHeaders(HttpHeaders.LOCATION)
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true).maxAge(3600)
                .allowedOrigins("http://localhost:3000");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("*")  // 모든 도메인 허용
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("Set-Cookie")
//                .allowCredentials(true).maxAge(3600);
//    }
}