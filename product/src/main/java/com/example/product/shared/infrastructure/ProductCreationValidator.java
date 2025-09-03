package com.example.product.shared.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.product.api.v1.dto.jsonapi.CreateProductAttributes;
import com.example.product.api.v1.dto.jsonapi.CreateProductJsonApiRequest;

@Component
public class ProductCreationValidator implements Validator {

    private final ValidationsConfig validationsConfig;

    public ProductCreationValidator(ValidationsConfig validationsConfig) {
        this.validationsConfig = validationsConfig;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateProductJsonApiRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateProductJsonApiRequest request = (CreateProductJsonApiRequest) target;
        CreateProductAttributes attr = request.data().attributes();

        if (attr.name().length() > validationsConfig.getNameMaxLength()) {
            errors.rejectValue("data.attributes.name", "name.length", "Product name cannot exceed " + validationsConfig.getNameMaxLength() + " characters.");
        }

        if (attr.description() != null && attr.description().length() > validationsConfig.getDescriptionMaxLength()) {
            errors.rejectValue("data.attributes.description", "description.length", "Description cannot exceed " + validationsConfig.getDescriptionMaxLength() + " characters.");
        }

        if (attr.price().compareTo(validationsConfig.getPriceMinValue()) < 0) {
            errors.rejectValue("data.attributes.price", "price.min", "Price must be at least " + validationsConfig.getPriceMinValue());
        }
        if (attr.price().compareTo(validationsConfig.getPriceMaxValue()) > 0) {
            errors.rejectValue("data.attributes.price", "price.max", "Price value is too high, max is " + validationsConfig.getPriceMaxValue());
        }
    }
}
