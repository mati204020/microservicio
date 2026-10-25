package com.service.disponibilidad.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contacto = new Contact();
        contacto.setName("Akatellme");
        contacto.setEmail("contacto@akatellme.com");

        Info info = new Info()
                .title("API de Disponibilidad")
                .version("1.0")
                .description("Microservicio encargado de gestionar las disponibilidades de las sesiones fotográficas")
                .contact(contacto);

        return new OpenAPI()
                .info(info);
    }
}