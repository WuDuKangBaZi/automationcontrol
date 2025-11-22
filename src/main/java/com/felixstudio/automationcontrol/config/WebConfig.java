// WebConfig.java
package com.felixstudio.automationcontrol.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadDir)
                        .setCachePeriod(60 * 60 * 24 * 30)
                        .resourceChain(true)
                        .addResolver(new PathResourceResolver());

                registry.addResourceHandler("/images/**")
                        .addResourceLocations("classpath:/static/images/");
            }
        };
    }
}
