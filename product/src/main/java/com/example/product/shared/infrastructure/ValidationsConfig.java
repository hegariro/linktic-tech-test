package com.example.product.shared.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "app.products")
@Getter
@Setter
public class ValidationsConfig {
    private int nameMaxLength;
    private int descriptionMaxLength;
    private BigDecimal priceMinValue;
    private BigDecimal priceMaxValue;
}
