package com.libms.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String CLASSPATH_RESOURCE_LOCATION = "classpath:/static/";
    private static final String EXTERNAL_RESOURCE_LOCATION = "file:///C:/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("static/css/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATION + "css/");
        registry.addResourceHandler("static/js/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATION + "js/");
        registry.addResourceHandler("static/images/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATION + "images/");
        registry.addResourceHandler("/summernote_image/**")
                .addResourceLocations(EXTERNAL_RESOURCE_LOCATION + "summernote_image/");
    }
}

//registry.addResourceHandler("/summernote_image/**")
//        .addResourceLocations("file:///C:/summernote_image/");