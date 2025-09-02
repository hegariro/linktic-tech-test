package com.example.product.shared.infrastructure;

import com.example.product.api.v1.dto.CreateProductRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.math.BigDecimal;

@Component
public class ProductCreationValidator implements Validator {

    private final ValidationsConfig validationsConfig;

    public ProductCreationValidator(ValidationsConfig validationsConfig) {
        this.validationsConfig = validationsConfig;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateProductRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateProductRequest request = (CreateProductRequest) target;

        if (request.name().length() > validationsConfig.getNameMaxLength()) {
            errors.rejectValue("name", "name.length", "Product name cannot exceed " + validationsConfig.getNameMaxLength() + " characters.");
        }

        if (request.description() != null && request.description().length() > validationsConfig.getDescriptionMaxLength()) {
            errors.rejectValue("description", "description.length", "Description cannot exceed " + validationsConfig.getDescriptionMaxLength() + " characters.");
        }

        if (request.price() != null) {
            if (request.price().compareTo(validationsConfig.getPriceMinValue()) < 0) {
                errors.rejectValue("price", "price.min", "Price must be at least " + validationsConfig.getPriceMinValue());
            }
            if (request.price().compareTo(validationsConfig.getPriceMaxValue()) > 0) {
                errors.rejectValue("price", "price.max", "Price value is too high, max is " + validationsConfig.getPriceMaxValue());
            }
        }
    }
}
