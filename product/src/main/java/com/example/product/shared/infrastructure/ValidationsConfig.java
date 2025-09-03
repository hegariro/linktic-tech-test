package com.example.product.shared.infrastructure;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app.products")
@Getter
@Setter
public class ValidationsConfig {
    private int nameMaxLength;
    private int descriptionMaxLength;
    private BigDecimal priceMinValue;
    private BigDecimal priceMaxValue;
}
