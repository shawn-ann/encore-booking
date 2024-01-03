package com.lab.dev.shawn.api.config;

import com.lab.dev.shawn.api.aspect.AdminAuthInterceptor;
import com.lab.dev.shawn.api.aspect.WxAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthInterceptor()).excludePathPatterns(List.of("/admin/user/login","/admin/user/logout","/error","/404"))
                .addPathPatterns("/admin/**"); // 匹配所有路径

        registry.addInterceptor(new WxAuthInterceptor()).excludePathPatterns(List.of("/wx/user/login","/wx/user/send_sms_code/**"))
                .addPathPatterns("/wx/**"); // 匹配所有路径
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // 允许访问的前端应用程序地址
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
