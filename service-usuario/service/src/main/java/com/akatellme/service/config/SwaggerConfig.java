package com.akatellme.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Usuarios")
                        .version("1.0.0")
                        .description("Documentación del microservicio encargado de gestionar usuarios, tipos de usuario y tipos de sesión.")
                        .contact(new Contact()
                                .name("Equipo Akatellme")
                                .email("contacto@akatellme.cl"))
                        .license(new License()
                                .name("Uso académico")
                                .url("https://duoc.cl")));
    }
}