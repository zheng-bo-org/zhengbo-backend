package org.zhengbo.backend.global_exceptions;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        try {
            resolvers.add(0, new GlobalExceptionHandler());
        }catch (Exception ex) {
            System.exit(9999);
        }
    }
}
