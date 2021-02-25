package com.libms.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String CLASSPATH_RESOURCE_LOCATION = "classpath:/static/";
    private static final String EXTERNAL_RESOURCE_LOCATION = "file:///C:/";

    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:8090/summernoteImage/1234.jpg
    //로 접속하면 C:/summernote_image/1234.jpg 파일을 불러온다.
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