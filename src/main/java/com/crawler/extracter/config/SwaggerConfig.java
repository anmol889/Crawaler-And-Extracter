package com.crawler.extracter.config;

import com.crawler.extracter.constants.SwaggerApiInfoConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.crawler.extracter"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                SwaggerApiInfoConstants.TITLE,
                SwaggerApiInfoConstants.DESCRIPTION,
                "1.0",
                "Terms of Service",
                new Contact(SwaggerApiInfoConstants.CONTACT_NAME, SwaggerApiInfoConstants.CONTACT_URL,
                        SwaggerApiInfoConstants.CONTACT_EMAIL),
                "",
                ""
        );
    }

}
