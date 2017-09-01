package com.example;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Szymon on 21.03.2017.
 */
@Configuration
@EnableSwagger2
public class SwagerConfig {

    @Bean
    public Docket SwaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("events")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(getSwaggerPaths())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Documentation API BL")
                .description("Model Business Logic")
                .contact(new Contact("Szymon Piszczatowski",
                        "https://peaceful-caverns-69997.herokuapp.com",
                        "piszczatowskiszymon@gmail.com"))
                .license("License RSI Team")
                .licenseUrl("https://peaceful-caverns-69997.herokuapp.com")
                .version("1.0.1")
                .build();
    }

    private Predicate<String> getSwaggerPaths() {
        return or(
                regex("/api.*"),
                regex("/test.*"));
    }
}
