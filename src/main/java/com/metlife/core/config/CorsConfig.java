package com.metlife.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private static final String ALLOWED_ORIGINS = "";

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(ALLOWED_ORIGINS)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}