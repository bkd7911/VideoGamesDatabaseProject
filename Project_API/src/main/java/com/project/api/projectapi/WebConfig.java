///////////////////////////////////////////////////////////////////////////////////////////////////////
package com.project.api.projectapi;

//  FILE : WebConfig.java
//  AUTHOR :  Auto-Generated by using https://start.spring.io/
//  DESCRIPTION: Used to configure methods and which types of mappings are allowed
//               DO NOT change this file unless necessary
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }
}