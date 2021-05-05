package pers.kamisaka.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig{
    @Bean
    public Docket createRestApi(){
        //依次代表创建SWAGGER2类型的API文档、设置API的基本信息、select用于创建接口扫描器，apis设置了它的扫描方式
        // ，paths设置了要显示在文档上的url
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("pers.kamisaka.blog.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo getApiInfo(){
        //contact代表作者名，termsOfServiceUrl代表项目地址
        return new ApiInfoBuilder()
                .title("KamisakaNote's API document")
                .description("KamisakaNote的后端API文档")
                .termsOfServiceUrl("localhost:8080/blog")
                .contact(new Contact("Kamisaka","","wei1230120@qq.com"))
                .version("1.0")
                .build();
    }
}
