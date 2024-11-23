package org.resumeoptimizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CsrfTokenInterceptor csrfTokenInterceptor;

    @Autowired
    public WebConfig(CsrfTokenInterceptor csrfTokenInterceptor) {
        this.csrfTokenInterceptor = csrfTokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfTokenInterceptor);
    }
}
