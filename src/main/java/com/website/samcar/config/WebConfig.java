// WebConfig.java
package com.website.samcar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Maps /uploads/** to the local filesystem path
        registry.addResourceHandler("/images/**")
        		.addResourceLocations("file:uploads/images/");
    }
}
