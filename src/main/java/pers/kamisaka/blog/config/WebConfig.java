package pers.kamisaka.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.kamisaka.blog.interceptor.TokenAuthenticationInterceptor;

@Configuration
//因为这个项目跨域的配置选择通过继承WebMvcConfigurationSupport生效，所以这里也选择这种做法
//注意，如果不这样做会导致配置失效！！
public class WebConfig extends WebMvcConfigurationSupport {
    @Autowired
    TokenAuthenticationInterceptor authenticationInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor)
//                .addPathPatterns("/blog/admin/**")
//                .excludePathPatterns("/blog/admin/login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/blog/avatar/**").addResourceLocations("classpath:/static/avatar/");
    }
}
