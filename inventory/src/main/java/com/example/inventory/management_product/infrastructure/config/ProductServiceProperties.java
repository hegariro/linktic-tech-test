package com.example.inventory.management_product.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.services")
public class ProductServiceProperties {
    
    private String productUrl;
    private String productUser;
    private String productPassword;
    private int productTimeoutConnection;
    private int productTimeoutRead;

}
