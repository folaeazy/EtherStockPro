package com.fola.EtherStockPro.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EtherStockPro Inventory Management API")
                        .version("1.0")
                        .description("API documentation for the Inventory Management System")
                        .contact(new Contact().name("Fola Israel").email("folaeazy0423@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}
