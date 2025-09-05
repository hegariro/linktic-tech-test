package com.example.inventory.shared.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Sevicio Backend para la gestión de Productos",
    version = "1.0",
    description = "Documentación de la API para la gestión de productos."
))
public class OpenApiConfig {
    // Esta clase no necesita más código. Las anotaciones hacen todo el trabajo.
}

