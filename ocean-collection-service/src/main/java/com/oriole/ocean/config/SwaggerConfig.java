package com.oriole.ocean.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi createRestApi() {
        return GroupedOpenApi.builder()
                .group("wtf")
                .packagesToScan("com.oriole.ocean")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("Lib4Univ开源高校文库项目 RESTful API列表")
                .description("注意不得在生产环境启用 Swagger")
                .version("2.1")
                .contact(new Contact()
                        .name("基于深度遗忘的程序猿")
                        .url("http://www.oceanlibrary.cn")
                        .email("null"))
                .termsOfService("http://www.oceanlibrary.cn/")
                .license(new License()
                        .name("Apache2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}

