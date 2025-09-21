package com.barbosa.ms.invetorymgmt.productorder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // permite todas as rotas
                .allowedOrigins("http://localhost:4200") // permite o front-end Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                .allowedHeaders("*") // permite todos os cabeçalhos
                .allowCredentials(true); // permite cookies/autenticação
    }
}
