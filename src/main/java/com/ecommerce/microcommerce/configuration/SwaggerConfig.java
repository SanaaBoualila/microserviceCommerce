package com.ecommerce.microcommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
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
                .apis(RequestHandlerSelectors.basePackage("com.ecommerce.microcommerce.web"))
                //Nous utilisons la méthode basePackage du prédicat RequestHandlerSelectors afin
                //de demander à Swagger de ne rien documenter en dehors du package "web" qui contient notre code.
                .paths(PathSelectors.regex("/Produits.*"))
                //PathSelectors.regex("/Produits.*") permet de passer une expression régulière qui n'accepte
                // que les URI commençant par /Produits.
                .build();
    }

}
